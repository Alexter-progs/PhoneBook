package com.alexterprogs.PDF;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PageEventListener extends PdfPageEventHelper {
    private final BookPrinter bookPrinter;

    public PageEventListener(BookPrinter bookPrinter){
        this.bookPrinter = bookPrinter;
    }

    @Override
    public void onEndPage (PdfWriter writer, Document document) {
        PdfContentByte canvas = writer.getDirectContent();

        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_CENTER,
                new Phrase(Integer.toString(writer.getPageNumber()), bookPrinter.TIMES_12_NORMAL),
                document.getPageSize().getWidth() / 2,
                document.getPageSize().getBottom() + document.bottomMargin() - 25,
                0);
    }
}
