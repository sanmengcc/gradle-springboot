package com.example.pdfwatermarkV3.util;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.exceptions.BadPasswordException;
import com.itextpdf.text.exceptions.InvalidPdfException;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.*;

public class PdfPassword {

    private static MyPdfReader pdfReader = null;

    /**
     * 解密PDF
     * @return
     * @throws Exception
     */
    public static File unProtected(File file) throws Exception {
        ByteArrayOutputStream bos = null;
        ByteArrayInputStream bis = null;

        try {
            bos = new ByteArrayOutputStream();
            bis = new ByteArrayInputStream(FileUtil.file2bytes(file));

            initPdfReader(file.getPath());

            if (checkProtected()) {
                unProtectedPdf(bos);
                byte[] newBytes = bos.toByteArray();
                bos.flush();
                return FileUtil.bytes2File(newBytes, file.getPath() + "unProtected.pdf");
            } else {
                return FileUtil.bytes2File(bos.toByteArray(), file.getPath() + "unProtected.pdf");
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (null != bos) {
                bos.close();
            }
            if (null != bis) {
                bis.close();
            }
        }
    }

    private static void initPdfReader(String filePath) throws BadPasswordException {
        try {
            pdfReader = new MyPdfReader(filePath);
        } catch (BadPasswordException e) {
            throw new BadPasswordException("Bad password. It should be user password.");
        } catch (IOException e) {
            return;
        }
    }

    private static boolean checkProtected() throws InvalidPdfException {
        if (null == pdfReader) {
            throw new InvalidPdfException("Invalid pdf.");
        }
        if (pdfReader.isEncrypted()) {
            return true;
        } else {
            return false;
        }
    }

    private static void unProtectedPdf(OutputStream os) {
        PdfStamper stamper = null;
        try {
            MyPdfReader.unethicalreading = true;
            pdfReader.decryptOnPurpose();

            stamper = new PdfStamper(pdfReader, os);
        } catch (DocumentException e) {
            return;
        } catch (IOException e) {
            return;
        } finally {
            try {
                if (null != stamper) {
                    stamper.close();
                }
            } catch (Exception e) {
                return;
            }
        }
    }

}