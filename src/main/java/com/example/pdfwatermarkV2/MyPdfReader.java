package com.example.pdfwatermarkV2;

import com.itextpdf.text.pdf.PdfReader;

import java.io.IOException;

public class MyPdfReader extends PdfReader {
    public MyPdfReader(final String filename) throws IOException {
        super(filename);
    }

    public void decryptOnPurpose() {
        encrypted = false;
    }
}