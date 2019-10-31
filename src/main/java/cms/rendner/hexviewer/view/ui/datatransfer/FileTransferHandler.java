package cms.rendner.hexviewer.view.ui.datatransfer;

import cms.rendner.hexviewer.common.utils.ByteSizeConstants;
import cms.rendner.hexviewer.model.data.IDataModel;
import cms.rendner.hexviewer.model.data.file.FileData;
import cms.rendner.hexviewer.model.data.file.MappedFileData;
import cms.rendner.hexviewer.view.JHexViewer;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.List;

/**
 * @author rendner
 */
public class FileTransferHandler extends TransferHandler
{
    /**
     * Maximal size for small files.
     */
    private static final int SMALL_FILE_SIZE_LIMIT = ByteSizeConstants.ONE_MB;

    public boolean canImport(final TransferHandler.TransferSupport info)
    {
        return info.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
    }

    @SuppressWarnings("unchecked")
    public boolean importData(final TransferHandler.TransferSupport info)
    {
        if (!info.isDrop() || !canImport(info))
        {
            return false;
        }

        final Transferable t = info.getTransferable();
        final List<File> data;
        try
        {
            data = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
        }
        catch (Exception e)
        {
            return false;
        }

        if (!data.isEmpty())
        {
            for (final File file : data)
            {
                if (file.isFile())
                {
                    try
                    {
                        final IDataModel model;

                        if (file.length() > SMALL_FILE_SIZE_LIMIT)
                        {
                            model = new MappedFileData(file);
                        }
                        else
                        {
                            model = new FileData(file);
                        }

                        final JHexViewer hexViewer = (JHexViewer) info.getComponent();
                        hexViewer.setDataModel(model);
                        hexViewer.requestFocus();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                        return false;
                    }
                    break;
                }
            }
        }

        return true;
    }
}
