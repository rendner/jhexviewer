package example;

import cms.rendner.hexviewer.core.view.areas.AreaId;
import cms.rendner.hexviewer.support.data.formatter.IRowWiseByteFormatter;
import cms.rendner.hexviewer.support.data.formatter.RowWiseByteFormatter;
import cms.rendner.hexviewer.support.data.visitor.ByteVisitor;
import cms.rendner.hexviewer.support.data.visitor.IByteVisitor;
import cms.rendner.hexviewer.support.data.visitor.IRowWiseByteVisitor;
import cms.rendner.hexviewer.support.data.visitor.RowWiseByteVisitor;
import cms.rendner.hexviewer.support.data.visitor.consumer.IConsumer;
import cms.rendner.hexviewer.support.data.visitor.consumer.ToFileConsumer;
import cms.rendner.hexviewer.support.data.visitor.consumer.ToStringConsumer;
import cms.rendner.hexviewer.support.data.walker.ByteWalker;
import cms.rendner.hexviewer.support.data.walker.RowWiseByteWalker;
import cms.rendner.hexviewer.core.view.IContextMenuFactory;
import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.view.caret.ICaret;
import cms.rendner.hexviewer.core.view.highlight.IHighlighter;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rendner
 */
public class ExampleContextMenuFactory implements IContextMenuFactory
{
    private JFileChooser fileChooser;

    public JPopupMenu create(final JHexViewer hexViewer, final AreaId areaId, final int byteIndex)
    {
        final JPopupMenu menu = new JPopupMenu();

        addHighlightMenu(menu, hexViewer, byteIndex);

        menu.addSeparator();

        addCopyToConsole(menu, hexViewer);
        addCopyToClipboard(menu, hexViewer);
        addCopyToFile(menu, hexViewer);

        return menu;
    }

    private void addHighlightMenu(final JPopupMenu menu, final JHexViewer hexViewer, final int byteIndex)
    {
        final IHighlighter highlighter = hexViewer.getHighlighter();
        final ICaret caret = hexViewer.getCaret();

        menu.add(new JMenuItem(new AbstractAction("selection to highlight")
        {
            @Override
            public boolean isEnabled()
            {
                return highlighter != null && caret != null && !caret.isSelectionEmpty();
            }

            public void actionPerformed(final ActionEvent e)
            {
                highlighter.addHighlight(caret.getSelectionStart(), caret.getSelectionEnd());
                caret.clearSelection();
            }
        }));

        final List<IHighlighter.IHighlight> highlightsAtByte = new ArrayList<>();
        if (highlighter != null)
        {
            for (final IHighlighter.IHighlight highlight : highlighter.getHighlights())
            {
                if (highlight.getStartOffset() <= byteIndex && highlight.getEndOffset() >= byteIndex)
                {
                    highlightsAtByte.add(highlight);
                }
            }
        }

        menu.add(new JMenuItem(new AbstractAction("remove clicked highlight")
        {
            @Override
            public boolean isEnabled()
            {
                return highlighter != null && !highlightsAtByte.isEmpty();
            }

            public void actionPerformed(final ActionEvent e)
            {
                if (highlighter != null)
                {
                    highlighter.removeHighlights(highlightsAtByte);
                }
            }
        }));

        menu.add(new JMenuItem(new AbstractAction("remove all highlights")
        {
            @Override
            public boolean isEnabled()
            {
                return highlighter != null && highlighter.getHighlightsCount() > 0;
            }

            public void actionPerformed(final ActionEvent e)
            {
                if (highlighter != null)
                {
                    highlighter.removeHighlights(highlighter.getHighlights());
                }
            }
        }));
    }

    private void addCopyToConsole(final JPopupMenu menu, final JHexViewer hexViewer)
    {
        final JMenu copyMenu = new JMenu("copy bytes to console");
        copyMenu.setEnabled(!hexViewer.getCaret().isSelectionEmpty());
        menu.add(copyMenu);

        copyMenu.add(new JMenuItem(new AbstractAction("hex stream")
        {
            public void actionPerformed(final ActionEvent e)
            {
                visitSelectedBytes(hexViewer, new ByteVisitor(hexViewer.getHexValueFormatter()));
            }
        }));

        copyMenu.add(new JMenuItem(new AbstractAction("ascii stream")
        {
            public void actionPerformed(final ActionEvent e)
            {
                visitSelectedBytes(hexViewer, new ByteVisitor(hexViewer.getAsciiValueFormatter()));
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
        copyMenu.add(new JMenuItem(new AbstractAction("offset ascii")
        {
            public void actionPerformed(final ActionEvent e)
            {
                visitSelectedBytes(hexViewer, getConfiguredByteVisitor(hexViewer, null, false, true));
            }
        }));
        copyMenu.add(new JMenuItem(new AbstractAction("offset hex ascii")
        {
            public void actionPerformed(final ActionEvent e)
            {
                visitSelectedBytes(hexViewer, getConfiguredByteVisitor(hexViewer, null, true, true));
            }
        }));
    }

    private void addCopyToClipboard(final JPopupMenu menu, final JHexViewer hexViewer)
    {
        final JMenu copyMenu = new JMenu("copy bytes to clipboard");
        copyMenu.setEnabled(!hexViewer.getCaret().isSelectionEmpty());
        menu.add(copyMenu);

        copyMenu.add(new JMenuItem(new AbstractAction("hex stream")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final ToStringConsumer consumer = new ToStringConsumer();
                visitSelectedBytes(hexViewer, new ByteVisitor(consumer, hexViewer.getHexValueFormatter()));
                copyToClipboard(consumer.content());
            }
        }));

        copyMenu.add(new JMenuItem(new AbstractAction("ascii stream")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final ToStringConsumer consumer = new ToStringConsumer();
                visitSelectedBytes(hexViewer, new ByteVisitor(consumer, hexViewer.getAsciiValueFormatter()));
                copyToClipboard(consumer.content());
            }
        }));

        copyMenu.addSeparator();

        copyMenu.add(new JMenuItem(new AbstractAction("offset hex")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final ToStringConsumer consumer = new ToStringConsumer();
                visitSelectedBytes(hexViewer, getConfiguredByteVisitor(hexViewer, consumer, true, false));
                copyToClipboard(consumer.content());
            }
        }));
        copyMenu.add(new JMenuItem(new AbstractAction("offset ascii")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final ToStringConsumer consumer = new ToStringConsumer();
                visitSelectedBytes(hexViewer, getConfiguredByteVisitor(hexViewer, consumer, false, true));
                copyToClipboard(consumer.content());
            }
        }));
        copyMenu.add(new JMenuItem(new AbstractAction("offset hex ascii")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final ToStringConsumer consumer = new ToStringConsumer();
                visitSelectedBytes(hexViewer, getConfiguredByteVisitor(hexViewer, consumer, true, true));
                copyToClipboard(consumer.content());
            }
        }));
    }

    private void addCopyToFile(final JPopupMenu menu, final JHexViewer hexViewer)
    {
        final JMenu copyMenu = new JMenu("copy bytes to file");
        copyMenu.setEnabled(!hexViewer.getCaret().isSelectionEmpty());
        menu.add(copyMenu);

        copyMenu.add(new JMenuItem(new AbstractAction("hex stream")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final File file = chooseFile(hexViewer);
                if (file != null)
                {
                    visitSelectedBytes(hexViewer,
                            new ByteVisitor(
                                    new ToFileConsumer(file),
                                    hexViewer.getHexValueFormatter()));
                }
            }
        }));

        copyMenu.add(new JMenuItem(new AbstractAction("ascii stream")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final File file = chooseFile(hexViewer);
                if (file != null)
                {
                    visitSelectedBytes(hexViewer,
                            new ByteVisitor(
                                    new ToFileConsumer(file),
                                    hexViewer.getAsciiValueFormatter()));
                }
            }
        }));

        copyMenu.addSeparator();

        copyMenu.add(new JMenuItem(new AbstractAction("offset hex")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final File file = chooseFile(hexViewer);
                if (file != null)
                {
                    visitSelectedBytes(hexViewer, getConfiguredByteVisitor(hexViewer, new ToFileConsumer(file), true, false));
                }
            }
        }));
        copyMenu.add(new JMenuItem(new AbstractAction("offset ascii")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final File file = chooseFile(hexViewer);
                if (file != null)
                {
                    visitSelectedBytes(hexViewer, getConfiguredByteVisitor(hexViewer, new ToFileConsumer(file), false, true));
                }
            }
        }));
        copyMenu.add(new JMenuItem(new AbstractAction("offset hex ascii")
        {
            public void actionPerformed(final ActionEvent e)
            {
                final File file = chooseFile(hexViewer);
                if (file != null)
                {
                    visitSelectedBytes(hexViewer, getConfiguredByteVisitor(hexViewer, new ToFileConsumer(file), true, true));
                }
            }
        }));
    }

    private RowWiseByteVisitor getConfiguredByteVisitor(final JHexViewer hexViewer, final IConsumer consumer, final boolean includeHexArea, final boolean includeAsciiArea)
    {
        final RowWiseByteVisitor result = new RowWiseByteVisitor(getConfiguredByteFormatter(hexViewer), consumer);
        result.setIncludeHexArea(includeHexArea);
        result.setIncludeAsciiArea(includeAsciiArea);
        return result;
    }

    private IRowWiseByteFormatter getConfiguredByteFormatter(final JHexViewer hexViewer)
    {
        return new RowWiseByteFormatter(hexViewer.getOffsetValueFormatter(),
                hexViewer.getHexValueFormatter(),
                hexViewer.getAsciiValueFormatter());
    }

    private void visitSelectedBytes(final JHexViewer hexViewer, final IRowWiseByteVisitor visitor)
    {
        final ICaret caret = hexViewer.getCaret();
        final RowWiseByteWalker walker = new RowWiseByteWalker(hexViewer.getDataModel(), hexViewer.bytesPerRow());
        walker.walk(visitor, caret.getSelectionStart(), caret.getSelectionEnd());
    }

    private void visitSelectedBytes(final JHexViewer hexViewer, final IByteVisitor visitor)
    {
        final ICaret caret = hexViewer.getCaret();
        final ByteWalker walker = new ByteWalker(hexViewer.getDataModel());
        walker.walk(visitor, caret.getSelectionStart(), caret.getSelectionEnd());
    }

    private void copyToClipboard(final String content)
    {
        final StringSelection selection = new StringSelection(content);
        final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

    private File chooseFile(final JHexViewer hexViewer)
    {
        if (fileChooser == null)
        {
            fileChooser = new JFileChooser();
        }

        final int result = fileChooser.showSaveDialog(hexViewer);

        if (result == JFileChooser.APPROVE_OPTION)
        {
            return fileChooser.getSelectedFile();
        }

        return null;
    }
}
