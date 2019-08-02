import cms.rendner.hexviewer.core.JHexViewer;
import cms.rendner.hexviewer.core.model.row.template.configuration.values.EMValue;
import cms.rendner.hexviewer.core.view.highlight.IHighlighter;
import example.DataModelFactory;
import example.ExampleContextMenuFactory;
import example.themes.ThemeFactory;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author rendner
 */
public class Main
{
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                displayJFrame();
            }
        });
    }

    static void displayJFrame()
    {
        final Main main = new Main();

        try
        {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        final JFrame frame = new JFrame("JHexViewer");

        final JComponent content = main.createHexViewer();
        content.addPropertyChangeListener(new PropertyChangeListener()
        {
            @Override
            public void propertyChange(final PropertyChangeEvent event)
            {
                if (event.getPropertyName().equals(JHexViewer.PROPERTY_DATA_MODEL))
                {
                    frame.pack();
                }
            }
        });

        frame.getContentPane().add(content);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private JComponent createHexViewer()
    {
        final JHexViewer hexViewer = new JHexViewer();
        hexViewer.setShowOffsetCaretIndicator(true);
        hexViewer.setDataModel(DataModelFactory.createRandomDataModel());
        hexViewer.setPreferredVisibleRowCount(23);
        hexViewer.setContextMenuFactory(new ExampleContextMenuFactory());

        ThemeFactory.applyRandomTheme(hexViewer);

        hexViewer.setRowTemplateConfiguration(
                hexViewer.getRowTemplateConfiguration().toBuilder()
                        .bytesPerRow(16)
                        .bytesPerGroup(2)
                        .spaceBetweenGroups(new EMValue(1))
                        .build()
        );

        final IHighlighter highlighter = hexViewer.getHighlighter();
        if (highlighter != null)
        {
            highlighter.setPaintSelectionBehindHighlights(false);
        }

        return hexViewer;
    }
}