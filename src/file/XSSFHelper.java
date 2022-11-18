package file;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class XSSFHelper {
    private XSSFWorkbook xssfWb;
    private XSSFSheet xssfSheet;
    private XSSFRow xssfRow;
    private XSSFCell xssfCell;
    private Font fontRed;
    private Font fontBlue;
    private Font fontGreen;
    private Font fontViolet;
    private Font fontIndigo;
    private Font fontDefault;
    private Font fontBoldRed;
    private Font fontBoldBlue;
    private Font fontLine;
    private Font fontBoldAndLineRed;
    private Font fontBoldAndLineBlue;
    public CellStyle styleOfDefault;
    public CellStyle styleOfFontRed;
    public CellStyle styleOfFontBlue;
    public CellStyle styleOfFontGreen;
    public CellStyle styleOfFontViolet;
    public CellStyle styleOfFontIndigo;
    public CellStyle styleOfTitle;
    public CellStyle styleOfHit;
    public CellStyle styleOfMiss;
    public CellStyle styleOfRef;
    public CellStyle styleOfHitAndRef;
    public CellStyle styleOfMissAndRef;

    public XSSFHelper() {
        initialization();
    }

    public void createRow(int row) {
        xssfRow = xssfSheet.createRow(row);
    }

    public void writeCell(int col, long number, CellStyle style) {
        xssfCell = xssfRow.createCell((short) col);
        xssfCell.setCellStyle(style);
        xssfCell.setCellValue(number);
    }

    public void writeCell(int col, String string, CellStyle style) {
        xssfCell = xssfRow.createCell((short) col);
        xssfCell.setCellStyle(style);
        xssfCell.setCellValue(string);
    }

    public void writeCell(int col, Double number, CellStyle style) {
        xssfCell = xssfRow.createCell((short) col);
        xssfCell.setCellStyle(style);
        xssfCell.setCellValue(number);
    }

    public void writeBufferLine(int col, long[] buffer, int hitBufferIdx, int missBufferIdx) {
        for (int i = 0; i < buffer.length; i++) {
            xssfCell = xssfRow.createCell((short) col + i);
            if (buffer[i] == -1)
                break;

            if (i == hitBufferIdx)
                xssfCell.setCellStyle(styleOfHit);
            else if (i == missBufferIdx)
                xssfCell.setCellStyle(styleOfMiss);
            else
                xssfCell.setCellStyle(styleOfDefault);
            xssfCell.setCellValue(buffer[i]);
        }
    }

    public void writeBufferLine(int col, long[] buffer, boolean[] rBit, int hitBufferIdx, int missBufferIdx) {
        for (int i = 0; i < buffer.length; i++) {
            xssfCell = xssfRow.createCell((short) col + i);
            if (buffer[i] == -1)
                break;

            if (i == hitBufferIdx) {
                if (rBit[i])
                    xssfCell.setCellStyle(styleOfHitAndRef);
                else
                    xssfCell.setCellStyle(styleOfHit);
            }
            else if (i == missBufferIdx) {
                if (rBit[i])
                    xssfCell.setCellStyle(styleOfMissAndRef);
                else
                    xssfCell.setCellStyle(styleOfMiss);
            }
            else {
                if (rBit[i])
                    xssfCell.setCellStyle(styleOfRef);
                else
                    xssfCell.setCellStyle(styleOfDefault);
            }
            xssfCell.setCellValue(buffer[i]);
        }
    }

    public String save(File file) {
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            xssfWb.write(fos);
            if(fos != null)
                fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    private void initialization() {
        xssfWb = new XSSFWorkbook();
        xssfSheet = xssfWb.createSheet("result");

        xssfSheet.addMergedRegion(new CellRangeAddress(1,1,1,2));

        fontDefault = xssfWb.createFont();
        fontDefault.setFontName("Calibri");

        fontRed = xssfWb.createFont();
        fontRed.setFontName("Calibri");
        fontRed.setColor(IndexedColors.RED.getIndex());

        fontBlue = xssfWb.createFont();
        fontBlue.setFontName("Calibri");
        fontBlue.setColor(IndexedColors.BLUE.getIndex());

        fontIndigo = xssfWb.createFont();
        fontIndigo.setFontName("Calibri");
        fontIndigo.setColor(IndexedColors.INDIGO.getIndex());

        fontBoldRed = xssfWb.createFont();
        fontBoldRed.setFontName("Calibri");
        fontBoldRed.setBold(true);
        fontBoldRed.setColor(IndexedColors.RED.getIndex());

        fontBoldBlue = xssfWb.createFont();
        fontBoldBlue.setFontName("Calibri");
        fontBoldBlue.setBold(true);
        fontBoldBlue.setColor(IndexedColors.BLUE.getIndex());

        fontGreen = xssfWb.createFont();
        fontGreen.setFontName("Calibri");
        fontGreen.setColor(IndexedColors.GREEN.getIndex());

        fontLine = xssfWb.createFont();
        fontLine.setFontName("Calibri");
        fontLine.setUnderline(Font.U_SINGLE);

        fontBoldAndLineRed = xssfWb.createFont();
        fontBoldAndLineRed.setFontName("Calibri");
        fontBoldAndLineRed.setBold(true);
        fontBoldAndLineRed.setColor(IndexedColors.RED.getIndex());
        fontBoldAndLineRed.setUnderline(Font.U_SINGLE);

        fontBoldAndLineBlue = xssfWb.createFont();
        fontBoldAndLineBlue.setFontName("Calibri");
        fontBoldAndLineBlue.setBold(true);
        fontBoldAndLineBlue.setColor(IndexedColors.BLUE.getIndex());
        fontBoldAndLineBlue.setUnderline(Font.U_SINGLE);

        fontViolet = xssfWb.createFont();
        fontViolet.setFontName("Calibri");
        fontViolet.setColor(IndexedColors.VIOLET.getIndex());

        styleOfDefault = xssfWb.createCellStyle();
        styleOfDefault.setAlignment(HorizontalAlignment.CENTER);
        styleOfDefault.setFont(fontDefault);

        styleOfFontRed = xssfWb.createCellStyle();
        styleOfFontRed.setAlignment(HorizontalAlignment.CENTER);
        styleOfFontRed.setFont(fontRed);

        styleOfFontBlue = xssfWb.createCellStyle();
        styleOfFontBlue.setAlignment(HorizontalAlignment.CENTER);
        styleOfFontBlue.setFont(fontBlue);

        styleOfFontIndigo = xssfWb.createCellStyle();
        styleOfFontIndigo.setAlignment(HorizontalAlignment.CENTER);
        styleOfFontIndigo.setFont(fontIndigo);

        styleOfHit = xssfWb.createCellStyle();
        styleOfHit.setAlignment(HorizontalAlignment.CENTER);
        styleOfHit.setFont(fontBoldBlue);

        styleOfMiss = xssfWb.createCellStyle();
        styleOfMiss.setAlignment(HorizontalAlignment.CENTER);
        styleOfMiss.setFont(fontBoldRed);

        styleOfFontGreen = xssfWb.createCellStyle();
        styleOfFontGreen.setAlignment(HorizontalAlignment.CENTER);
        styleOfFontGreen.setFont(fontGreen);

        styleOfFontViolet = xssfWb.createCellStyle();
        styleOfFontViolet.setAlignment(HorizontalAlignment.CENTER);
        styleOfFontViolet.setFont(fontViolet);

        styleOfTitle = xssfWb.createCellStyle();
        styleOfTitle.setAlignment(HorizontalAlignment.CENTER);
        styleOfTitle.setFont(fontDefault);
        styleOfTitle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        styleOfTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        styleOfRef = xssfWb.createCellStyle();
        styleOfRef.setAlignment(HorizontalAlignment.CENTER);
        styleOfRef.setFont(fontLine);

        styleOfHitAndRef = xssfWb.createCellStyle();
        styleOfHitAndRef.setAlignment(HorizontalAlignment.CENTER);
        styleOfHitAndRef.setFont(fontBoldAndLineBlue);

        styleOfMissAndRef = xssfWb.createCellStyle();
        styleOfMissAndRef.setAlignment(HorizontalAlignment.CENTER);
        styleOfMissAndRef.setFont(fontBoldAndLineRed);
    }
}
