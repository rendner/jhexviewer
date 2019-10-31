import cms.rendner.hexviewer.view.JHexViewer;
import example.DataModelFactory;
import example.ExampleContextMenuFactory;
import example.themes.ThemeFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author rendner
 */
public class Main
{
    public static void main(@NotNull final String[] args)
    {
        SwingUtilities.invokeLater(Main::displayJFrame);
    }

    private static void displayJFrame()
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
        frame.getContentPane().add(main.createHexViewer());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @NotNull
    private JHexViewer createHexViewer()
    {
        final JHexViewer hexViewer = new JHexViewer();
        hexViewer.setShowOffsetCaretIndicator(true);
        hexViewer.setPreferredVisibleRowCount(23);
        hexViewer.setBytesPerRow(16);
        hexViewer.setContextMenuFactory(new ExampleContextMenuFactory());
        hexViewer.setDataModel(DataModelFactory.createRandomDataModel(hexViewer.getBytesPerRow()));
        ThemeFactory.applyRandomTheme(hexViewer);
        return hexViewer;
    }
}