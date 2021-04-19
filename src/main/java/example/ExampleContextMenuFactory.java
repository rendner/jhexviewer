package example;

import cms.rendner.hexviewer.common.data.formatter.base.IValueFormatter;
import cms.rendner.hexviewer.common.data.formatter.offset.IOffsetFormatter;
import cms.rendner.hexviewer.common.data.visitor.ByteVisitor;
import cms.rendner.hexviewer.common.data.visitor.IByteVisitor;
import cms.rendner.hexviewer.common.data.visitor.IRowWiseByteVisitor;
import cms.rendner.hexviewer.common.data.visitor.RowWiseByteVisitor;
import cms.rendner.hexviewer.common.data.visitor.consumer.IDataConsumer;
import cms.rendner.hexviewer.common.data.visitor.consumer.ToFileConsumer;
import cms.rendner.hexviewer.common.data.visitor.consumer.ToStringConsumer;
import cms.rendner.hexviewer.common.data.visitor.formatter.IRowWiseByteFormatter;
import cms.rendner.hexviewer.common.data.visitor.formatter.RowWiseByteFormatter;
import cms.rendner.hexviewer.common.data.walker.ByteWalker;
import cms.rendner.hexviewer.common.data.walker.RowWiseByteWalker;
import cms.rendner.hexviewer.view.JHexViewer;
import cms.rendner.hexviewer.view.components.areas.common.AreaId;
import cms.rendner.hexviewer.view.components.highlighter.IHighlighter;
import cms.rendner.hexviewer.view.ui.container.IContextMenuFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Example implementation of a context menu factory.
 *
 * @author rendner
 */
public class ExampleContextMenuFactory implements IContextMenuFactory
{
    private JFileChooser fileChooser;

    @NotNull
    public JPopupMenu create(@NotNull final JHexViewer hexViewer, @NotNull final AreaId areaId, final long byteIndex)
    {
        final JPopupMenu menu = new JPopupMenu();

        addHighlightMenu(menu, hexViewer, byteIndex);

        menu.addSeparator();

        addCopyToConsole(menu, hexViewer);
        addCopyToClipboard(menu, hexViewer);
        addCopyToFile(menu, hexViewer);

        return menu;
    }

    private void addHighlightMenu(@NotNull final JPopupMenu menu, @NotNull final JHexViewer hexViewer, final long byteIndex)
    {
        menu.add(new JMenuItem(new AbstractAction("selection to highlight")
        {
            @Override
            public boolean isEnabled()
            {
                return hexViewer.getHighlighter().isPresent() && hexViewer.hasSelection();
            }

            public void actionPerformed(final ActionEvent e)
            {
                selectionToHighlight();

                clearSelection();
            }

            private void selectionToHighlight()
            {
                hexViewer.getHighlighter().ifPresent(highlighter ->
                {
                    hexViewer.getCaret().ifPresent(caret ->
                    {
                        highlighter.addHighlight(caret.getSelectionStart(), caret.getSelectionEnd());
                    });
                });
            }

            private void clearSelection()
            {
                hexViewer.getCaret().ifPresent(caret -> caret.moveCaretRelatively(0, false, false));
            }
        }));

        final List<IHighlighter.IHighlight> highlightsAtByte = hexViewer.getHighlighter()
                .map(h -> h.getHighlights()
                        .stream()
                        .filter(i -> (i.getStartOffset() <= byteIndex && i.getEndOffset() >= byteIndex))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());

        menu.add(new JMenuItem(new AbstractAction("remove highlights under click")
        {
            @Override
            public boolean isEnabled()
            {
                return !highlightsAtByte.isEmpty();
            }

            public void actionPerformed(final ActionEvent e)
            {
                hexViewer.getHighlighter().ifPresent(h -> h.removeHighlights(highlightsAtByte));
            }
        }));

        menu.add(new JMenuItem(new AbstractAction("remove all highlights")
        {
            @Override
            public boolean isEnabled()
            {
                return hexViewer.getHighlighter().map(IHighlighter::hasHighlights).orElse(Boolean.FALSE);
            }

            public void actionPerformed(final ActionEvent e)
            {
                hexViewer.getHighlighter().ifPresent(IHighlighter::removeAllHighlights);
            }
        }));
    }

    private void addCopyToConsole(@NotNull final JPopupMenu menu, @NotNull final JHexViewer hexViewer)
    {
        final JMenu copyMenu = new JMenu("copy selected bytes to console");
        copyMenu.setEnabled(hexViewer.hasSelection());
        menu.add(copyMenu);

        copyMenu.add(new JMenuItem(new AbstractAction("hex stream")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final IValueFormatter valueFormatter = hexViewer.getHexArea().getValueFormatter();
                visitSelectedBytes(hexViewer, new ByteVisitor(valueFormatter));
            }
        }));

        copyMenu.add(new JMenuItem(new AbstractAction("text stream")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final IValueFormatter valueFormatter = hexViewer.getTextArea().getValueFormatter();
                visitSelectedBytes(hexViewer, new ByteVisitor(valueFormatter));
            }
        }));

        copyMenu.addSeparator();

        copyMenu.add(new JMenuItem(new AbstractAction("offset hex")
        {
            public void actionPerformed(final ActionEvent e)
            {
                visitSelectedBytes(hexViewer, getConfiguredByteVisitor(hexViewer, null, true, false));
            }
        }));
        copyMenu.add(new JMenuItem(new AbstractAction("offset text")
        {
            public void actionPerformed(final ActionEvent e)
            {
                visitSelectedBytes(hexViewer, getConfiguredByteVisitor(hexViewer, null, false, true));
            }
        }));
        copyMenu.add(new JMenuItem(new AbstractAction("offset hex text")
        {
            public void actionPerformed(final ActionEvent e)
            {
                visitSelectedBytes(hexViewer, getConfiguredByteVisitor(hexViewer, null, true, true));
            }
        }));
    }

    private void addCopyToClipboard(@NotNull final JPopupMenu menu, @NotNull final JHexViewer hexViewer)
    {
        final JMenu copyMenu = new JMenu("copy selected bytes to clipboard");
        copyMenu.setEnabled(hexViewer.hasSelection());
        menu.add(copyMenu);

        copyMenu.add(new JMenuItem(new AbstractAction("hex stream")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final ToStringConsumer consumer = new ToStringConsumer(ExampleContextMenuFactory.this::copyToClipboard);
                final IValueFormatter valueFormatter = hexViewer.getHexArea().getValueFormatter();
                visitSelectedBytes(hexViewer, new ByteVisitor(consumer, valueFormatter));
            }
        }));

        copyMenu.add(new JMenuItem(new AbstractAction("text stream")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final ToStringConsumer consumer = new ToStringConsumer(ExampleContextMenuFactory.this::copyToClipboard);
                final IValueFormatter valueFormatter = hexViewer.getTextArea().getValueFormatter();
                visitSelectedBytes(hexViewer, new ByteVisitor(consumer, valueFormatter));
            }
        }));

        copyMenu.addSeparator();

        copyMenu.add(new JMenuItem(new AbstractAction("offset hex")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final ToStringConsumer consumer = new ToStringConsumer(ExampleContextMenuFactory.this::copyToClipboard);
                visitSelectedBytes(hexViewer, getConfiguredByteVisitor(hexViewer, consumer, true, false));
            }
        }));
        copyMenu.add(new JMenuItem(new AbstractAction("offset text")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final ToStringConsumer consumer = new ToStringConsumer(ExampleContextMenuFactory.this::copyToClipboard);
                visitSelectedBytes(hexViewer, getConfiguredByteVisitor(hexViewer, consumer, false, true));
            }
        }));
        copyMenu.add(new JMenuItem(new AbstractAction("offset hex text")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final ToStringConsumer consumer = new ToStringConsumer(ExampleContextMenuFactory.this::copyToClipboard);
                visitSelectedBytes(hexViewer, getConfiguredByteVisitor(hexViewer, consumer, true, true));
            }
        }));
    }

    private void addCopyToFile(@NotNull final JPopupMenu menu, @NotNull final JHexViewer hexViewer)
    {
        final JMenu copyMenu = new JMenu("copy selected bytes to file");
        copyMenu.setEnabled(hexViewer.hasSelection());
        menu.add(copyMenu);

        copyMenu.add(new JMenuItem(new AbstractAction("hex stream")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final Path path = chooseFile(hexViewer);
                if (path != null)
                {
                    final IValueFormatter valueFormatter = hexViewer.getHexArea().getValueFormatter();
                    visitSelectedBytes(hexViewer,
                            new ByteVisitor(
                                    new ToFileConsumer(path),
                                    valueFormatter));
                }
            }
        }));

        copyMenu.add(new JMenuItem(new AbstractAction("text stream")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final Path path = chooseFile(hexViewer);
                if (path != null)
                {
                    final IValueFormatter valueFormatter = hexViewer.getTextArea().getValueFormatter();
                    visitSelectedBytes(hexViewer,
                            new ByteVisitor(
                                    new ToFileConsumer(path),
                                    valueFormatter));
                }
            }
        }));

        copyMenu.addSeparator();

        copyMenu.add(new JMenuItem(new AbstractAction("offset hex")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final Path path = chooseFile(hexViewer);
                if (path != null)
                {
                    visitSelectedBytes(hexViewer, getConfiguredByteVisitor(hexViewer, new ToFileConsumer(path), true, false));
                }
            }
        }));
        copyMenu.add(new JMenuItem(new AbstractAction("offset text")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final Path path = chooseFile(hexViewer);
                if (path != null)
                {
                    visitSelectedBytes(hexViewer, getConfiguredByteVisitor(hexViewer, new ToFileConsumer(path), false, true));
                }
            }
        }));
        copyMenu.add(new JMenuItem(new AbstractAction("offset hex text")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final Path path = chooseFile(hexViewer);
                if (path != null)
                {
                    visitSelectedBytes(hexViewer, getConfiguredByteVisitor(hexViewer, new ToFileConsumer(path), true, true));
                }
            }
        }));
    }

    @NotNull
    private RowWiseByteVisitor getConfiguredByteVisitor(@NotNull final JHexViewer hexViewer, @Nullable final IDataConsumer consumer, final boolean includeHexArea, final boolean includeTextArea)
    {
        final RowWiseByteVisitor result = new RowWiseByteVisitor(getConfiguredByteFormatter(hexViewer), consumer, hexViewer.getBytesPerRow());
        result.setIncludeHexArea(includeHexArea);
        result.setIncludeTextArea(includeTextArea);
        return result;
    }

    @NotNull
    private IRowWiseByteFormatter getConfiguredByteFormatter(@NotNull final JHexViewer hexViewer)
    {
        final IOffsetFormatter offsetValueFormatter = hexViewer.getOffsetArea().getValueFormatter();
        final IValueFormatter hexValueFormatter = hexViewer.getHexArea().getValueFormatter();
        final IValueFormatter textValueFormatter = hexViewer.getTextArea().getValueFormatter();
        return new RowWiseByteFormatter(hexViewer.getBytesPerRow(), offsetValueFormatter, hexValueFormatter, textValueFormatter);
    }

    private void visitSelectedBytes(@NotNull final JHexViewer hexViewer, @NotNull final IRowWiseByteVisitor visitor)
    {
        hexViewer.getDataModel().ifPresent(bytes ->
        {
            hexViewer.getCaret().ifPresent(caret -> {
                final ExecutorService executor = Executors.newSingleThreadExecutor();
                try
                {
                    executor.execute(() -> {
                        final RowWiseByteWalker walker = new RowWiseByteWalker(bytes, hexViewer.getBytesPerRow());
                        walker.walk(visitor, caret.getSelectionStart(), caret.getSelectionEnd());
                    });
                }
                finally
                {
                    executor.shutdown();
                }
            });
        });
    }

    private void visitSelectedBytes(@NotNull final JHexViewer hexViewer, @NotNull final IByteVisitor visitor)
    {
        hexViewer.getDataModel().ifPresent(bytes ->
        {
            hexViewer.getCaret().ifPresent(caret -> {
                final ExecutorService executor = Executors.newSingleThreadExecutor();
                try
                {
                    executor.execute(() -> {
                        final ByteWalker walker = new ByteWalker(bytes);
                        walker.walk(visitor, caret.getSelectionStart(), caret.getSelectionEnd());
                    });
                }
                finally
                {
                    executor.shutdown();
                }
            });
        });
    }

    private void copyToClipboard(@NotNull final String content)
    {
        final StringSelection selection = new StringSelection(content);
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    @Nullable
    private Path chooseFile(@NotNull final JHexViewer hexViewer)
    {
        if (fileChooser == null)
        {
            fileChooser = new JFileChooser();
        }

        final int result = fileChooser.showSaveDialog(hexViewer);

        if (result == JFileChooser.APPROVE_OPTION)
        {
            return fileChooser.getSelectedFile().toPath();
        }

        return null;
    }
}
