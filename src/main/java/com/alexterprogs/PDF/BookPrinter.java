package com.alexterprogs.PDF;

import com.alexterprogs.Person;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.*;

public class BookPrinter {
    public Font TIMES_12_NORMAL;
    public Font TIMES_16_BOLD;
    public Font TIMES_14_BOLD;
    public Font TIMES_12_BOLD;
    public Font TIMES_12_ITALIC;
    private final String TITLE_IMAGE = "Icons/FGUPRNC.jpg";


    public BookPrinter () {
        try {
            BaseFont bf = BaseFont.createFont("Fonts/Times.ttf", BaseFont.IDENTITY_H , BaseFont.EMBEDDED);
            TIMES_12_NORMAL = new Font(bf, 12, Font.NORMAL);
            TIMES_16_BOLD = new Font(bf, 16, Font.BOLD);
            TIMES_14_BOLD = new Font(bf, 14, Font.BOLD);
            TIMES_12_BOLD = new Font(bf, 12, Font.BOLD);
            TIMES_12_ITALIC = new Font(bf, 12, Font.ITALIC);
        } catch (DocumentException | IOException e){
            e.printStackTrace();
        }
    }

    public void save(String str, DefaultTableModel tblModel){
        try {
            FileOutputStream fos = new FileOutputStream(str);
            generateBook(fos, tblModel, true);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void print(DefaultTableModel tblModel){
        //ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //generateBook(baos, tblModel, false);

    }

    private void generateBook(OutputStream os, DefaultTableModel tblModel, boolean isCloseStream){
        try {
            Document doc = new Document();
            PdfWriter writer = PdfWriter.getInstance(doc, os);

            writer.setCloseStream(isCloseStream);

            doc.open();
            writeTitlePageWithImage(doc, writer);
            doc.newPage();
            writeTitlePageWithAdress(doc, writer);
            doc.newPage();
            writeTitlePageWithDutyService(doc, writer);
            doc.newPage();
            writer.setPageEvent(new PageEventListener(this));
            writePersonsTable(doc, writer, tblModel);
            doc.newPage();

            doc.close();

        } catch (DocumentException | IOException ex){
            ex.printStackTrace();
        }
    }

    private void writeTitlePageWithImage (Document doc, PdfWriter writer) throws DocumentException, IOException{
        Paragraph title = new Paragraph("ТЕЛЕФОННЫЙ СПРАВОЧНИК", TIMES_16_BOLD);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(25);
        doc.add(title);

        Paragraph title2 = new Paragraph("ПРОЕКТНОГО ИНСТИТУТА ФГУП \n \"РНЦ \"ПРИКЛАДНАЯ ХИМИЯ\"", TIMES_14_BOLD);
        title2.setAlignment(Element.ALIGN_CENTER);
        doc.add(title2);

        PdfContentByte canvas = writer.getDirectContent();

        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_CENTER,
                new Phrase((Integer.toString(Calendar.getInstance().get(Calendar.YEAR)) + " год"), TIMES_12_NORMAL),
                doc.getPageSize().getWidth() / 2,
                doc.getPageSize().getBottom() + doc.bottomMargin() + 10,
                0);

        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_CENTER,
                new Phrase("Санкт-Петербург", TIMES_12_NORMAL),
                doc.getPageSize().getWidth() / 2,
                doc.getPageSize().getBottom() + doc.bottomMargin() + 25,
                0);


        Image img = Image.getInstance(getClass().getClassLoader().getResource(TITLE_IMAGE));
        img.setBorderColor(BaseColor.BLACK);
        img.setBorderWidth(1);
        img.setBorder(Image.BOX);
        img.setAlignment(Image.ALIGN_CENTER);
        img.scaleToFit(512, 768);
        img.setAbsolutePosition(
                ((doc.getPageSize().getWidth() - img.getScaledWidth() / 2) - doc.getPageSize().getWidth() / 2),
                ((doc.getPageSize().getHeight() - img.getScaledHeight()) / 2));

        writer.getDirectContent().addImage(img);
    }

    private void writeTitlePageWithAdress (Document doc, PdfWriter writer) throws DocumentException{
        PdfContentByte canvas = writer.getDirectContent();

        float lly = doc.getPageSize().getBottom()  + doc.bottomMargin();
        float llx = doc.getPageSize().getLeft()    + doc.leftMargin() ;
        float ury = doc.getPageSize().getTop()     - doc.topMargin();
        float urx = doc.getPageSize().getRight()   - doc.rightMargin();

        Rectangle rect = new Rectangle(llx, lly, urx, ury);
        rect.setBorder(Rectangle.BOX);
        rect.setBorderWidth(2);
        canvas.rectangle(rect);

        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_CENTER,
                new Phrase("СПИСОК ТЕЛЕФОНОВ", TIMES_16_BOLD),
                doc.getPageSize().getWidth() / 2,
                doc.getPageSize().getHeight() / 2 + 100,
                0);

        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_CENTER,
                new Phrase("ПРОЕКТНОГО ИНСТИТУТА ФГУП", TIMES_16_BOLD),
                doc.getPageSize().getWidth() / 2,
                doc.getPageSize().getHeight() / 2 + 50,
                0);

        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_CENTER,
                new Phrase("\"РНЦ \"ПРИКЛАДНАЯ ХИМИЯ\"", TIMES_16_BOLD),
                doc.getPageSize().getWidth() / 2,
                doc.getPageSize().getHeight() / 2 + 25,
                0);

        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_CENTER,
                new Phrase("ПЛОЩАДКА \n УЛ. Крыленко Д. 26А", TIMES_16_BOLD),
                doc.getPageSize().getWidth() / 2,
                doc.getPageSize().getHeight() / 2 - 50,
                0);

        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_CENTER,
                new Phrase("УЛ. Крыленко Д. 26А", TIMES_16_BOLD),
                doc.getPageSize().getWidth() / 2,
                doc.getPageSize().getHeight() / 2 - 65,
                0);

        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_CENTER,
                new Phrase((Integer.toString(Calendar.getInstance().get(Calendar.YEAR)) + " год"), TIMES_12_NORMAL),
                doc.getPageSize().getWidth() / 2,
                doc.getPageSize().getBottom() + doc.bottomMargin() + 10,
                0);

        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_CENTER,
                new Phrase("Санкт-Петербург", TIMES_12_NORMAL),
                doc.getPageSize().getWidth() / 2,
                doc.getPageSize().getBottom() + doc.bottomMargin() + 25,
                0);
    }

    private void writeTitlePageWithDutyService (Document doc, PdfWriter writer) throws DocumentException{
        PdfContentByte canvas = writer.getDirectContent();

        float lly = doc.getPageSize().getBottom()  + doc.bottomMargin();
        float llx = doc.getPageSize().getLeft()    + doc.leftMargin() ;
        float ury = doc.getPageSize().getTop()     - doc.topMargin();
        float urx = doc.getPageSize().getRight()   - doc.rightMargin();

        Rectangle rect = new Rectangle(llx, lly, urx, ury);
        rect.setBorder(Rectangle.BOX);
        rect.setBorderWidth(2);
        canvas.rectangle(rect);

        PdfPTable services = new PdfPTable(2);
        float totalTableWidth = doc.getPageSize().getWidth() / 2.0f;
        float[] colWidths = { totalTableWidth - totalTableWidth / 10.0f, totalTableWidth / 10.0f };
        services.setTotalWidth(colWidths);

        String[] dutyServicesWithNumbers = {
                "Дежурный пожарной службы"          , "01",
                "Дежурный охраны"                   , "02",
                "Медицинская служба"                , "03",
                "Техническая поддержка телефонии"   , "04",
                "Инженерная служба"                 , "05",
                "Тех. поддержка компьютерных сетей" , "06"
        };

        new PdfPCell(new Phrase("Дежурный пожарной службы", TIMES_12_NORMAL)).setHorizontalAlignment(Element.ALIGN_LEFT);

        for (int i = 0; i < dutyServicesWithNumbers.length - 1 ; i+=2){
            PdfPCell serviceName = new PdfPCell(new Phrase(dutyServicesWithNumbers[i], TIMES_12_NORMAL));
            PdfPCell servicePhone = new PdfPCell(new Phrase(dutyServicesWithNumbers[i+1], TIMES_12_NORMAL));

            serviceName.setHorizontalAlignment(Element.ALIGN_LEFT);
            serviceName.setVerticalAlignment(Element.ALIGN_TOP);
            servicePhone.setHorizontalAlignment(Element.ALIGN_CENTER);
            servicePhone.setVerticalAlignment(Element.ALIGN_TOP);


            services.addCell(serviceName);
            services.addCell(servicePhone);
        }


        services.writeSelectedRows(0, -1,
                doc.getPageSize().getWidth()/2 - services.getTotalWidth()/2,
                doc.getPageSize().getHeight()/2 + services.getTotalHeight() * 2,
                canvas);

        ColumnText.showTextAligned(
                canvas,
                Element.ALIGN_CENTER,
                new Phrase("Дежурные службы", TIMES_14_BOLD),
                doc.getPageSize().getWidth() / 2,
                doc.getPageSize().getHeight()/2 + services.getTotalHeight() * 2 + 50,
                0);

        PdfPTable infoTable = new PdfPTable(1);
        infoTable.setTotalWidth(doc.getPageSize().getWidth() - doc.leftMargin() - doc.rightMargin() - 150);

        Paragraph info = new Paragraph();
        info.setSpacingAfter(20);

        info.add(new Phrase("№ Телефона, по которому нам", TIMES_12_BOLD));
        info.add(Chunk.NEWLINE);

        info.add(new Phrase(" могут звонить на местный телефон из города:", TIMES_12_BOLD));
        info.add(Chunk.NEWLINE);

        info.add(new Phrase("703-03-99, (или 647-92-77)", TIMES_12_BOLD));
        info.add(new Phrase(" после гудка набрать на кнопочном", TIMES_12_NORMAL));
        info.add(Chunk.NEWLINE);

        info.add(new Phrase("телефоне \"звёздочку\" (*) и добавочный (местный)", TIMES_12_NORMAL));
        info.add(new Phrase(" № ", TIMES_12_BOLD));
        info.add(new Phrase("телефона.", TIMES_12_NORMAL));

        PdfPCell infoCell = new PdfPCell(info);
        infoCell.setPaddingTop(10);
        infoCell.setPaddingBottom(10);
        infoCell.setBorderWidth(2);
        infoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        infoTable.addCell(infoCell);

        infoTable.writeSelectedRows(0, -1,
                doc.getPageSize().getWidth()/2 - infoTable.getTotalWidth()/2,
                doc.getPageSize().getHeight()/2 + infoTable.getTotalHeight() * 2 - 400,
                canvas);
    }

    private void writePersonsTable (Document doc, PdfWriter writer, DefaultTableModel tblModel) throws DocumentException{
        /* Table header */
        PdfPTable pTable = new PdfPTable(tblModel.getColumnCount() - 1);

        for (int i = 0; i < tblModel.getColumnCount() - 1; i++) {
            String str = tblModel.getColumnName(i);
            PdfPCell cell = new PdfPCell(new Phrase(str, TIMES_12_NORMAL));
            cell.setBackgroundColor(BaseColor.GRAY);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pTable.addCell(cell);
        }
        pTable.setHeaderRows(1);

        /* Initializing structure */
        Map<String, Set<Person>> personMap = new LinkedHashMap<>();
        for (int i = 0; i < tblModel.getRowCount(); i++){
            String key = tblModel.getValueAt(i, 4).toString();
            if (!personMap.containsKey(key)) {
                personMap.put(key, new LinkedHashSet<>());
            }
        }

        /* Filling structure with data */
        for (int i = 0; i < tblModel.getRowCount(); i++) {
            for (int j = 0; j < tblModel.getColumnCount() - 4; j += 5) {
                String jobPosition = tblModel.getValueAt(i, j).toString();
                String fullName = tblModel.getValueAt(i, j + 1).toString();
                String phoneNumber = tblModel.getValueAt(i, j + 2).toString();
                String roomNumber = tblModel.getValueAt(i, j + 3).toString();
                String department = tblModel.getValueAt(i, j + 4).toString();
                personMap.get(department).add(new Person(jobPosition, fullName, phoneNumber, roomNumber, department));

            }
        }

        /* Creating table */
        for (Map.Entry<String, Set<Person>> entry : personMap.entrySet()) {

            PdfPCell department = new PdfPCell(new Phrase(entry.getKey(), TIMES_12_ITALIC));
            department.setColspan(pTable.getNumberOfColumns());
            department.setHorizontalAlignment(Element.ALIGN_CENTER);
            department.setVerticalAlignment(Element.ALIGN_CENTER);
            department.setBackgroundColor(BaseColor.LIGHT_GRAY);
            pTable.addCell(department);

            for (Person p : entry.getValue()){
                PdfPCell job = new PdfPCell(new Phrase(p.getJobPosition(), TIMES_12_NORMAL));
                PdfPCell name = new PdfPCell(new Phrase(p.getFullName(), TIMES_12_NORMAL));
                PdfPCell phone = new PdfPCell(new Phrase(p.getRoomNumber(), TIMES_12_NORMAL));
                PdfPCell room = new PdfPCell(new Phrase(p.getRoomNumber(), TIMES_12_NORMAL));

                job.setHorizontalAlignment(Element.ALIGN_CENTER);
                job.setVerticalAlignment(Element.ALIGN_CENTER);
                name.setHorizontalAlignment(Element.ALIGN_CENTER);
                name.setVerticalAlignment(Element.ALIGN_CENTER);
                phone.setHorizontalAlignment(Element.ALIGN_CENTER);
                phone.setVerticalAlignment(Element.ALIGN_CENTER);
                room.setHorizontalAlignment(Element.ALIGN_CENTER);
                room.setVerticalAlignment(Element.ALIGN_CENTER);

                pTable.addCell(job);
                pTable.addCell(name);
                pTable.addCell(phone);
                pTable.addCell(room);
            }
        }
        doc.add(pTable);

    }
}
