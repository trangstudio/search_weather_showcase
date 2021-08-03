package core.util.platform.host.file;

import core.util.platform.host.os.OsHelper;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class FileHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class.getSimpleName());

    private FileHelper() { }

    public static String convertDirectory(String directory) {
        if (OsHelper.isWindows()) {
            LOGGER.info("We are working on Windows environment: " + OsHelper.getOsFullName());
            directory = directory.replace("/", "\\");
        } else {
            LOGGER.info("We are working on Linux / Mac environment: " + OsHelper.getOsFullName());
        }
        return directory;
    }

    public static void deleteAllFilesInFolder(String folderS){
        final File folder = new File(folderS);
        final File[] files = folder.listFiles( new FilenameFilter() {
            @Override
            public boolean accept( final File dir,
                                   final String name ) {
                return name.matches( "revert*\\.sql" );
            }
        } );
        for ( final File file : files ) {
            file.delete();
            if ( !file.delete() ) {
                System.err.println( "Can't remove " + file.getAbsolutePath() );
            }
        }
    }

    public static void deleteScreenshots() {
        try {
            String downloadFolderPath = System.getProperty("user.dir") + "/ReportNGScreenShots";
            File file = new File(downloadFolderPath);
            File[] listOfFiles = file.listFiles();
            if (listOfFiles.length == 0) {
                System.out.println("There is no files to delete");
            } else {
                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                        System.out.println("Deleted file " + listOfFiles[i].getName());
                        new File(listOfFiles[i].toString()).delete();
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void replaceTextFile(String inputFile, String outFile, String key, String newKey){
        try {
            Path path = Paths.get(inputFile);
            Charset charset = StandardCharsets.UTF_8;
            String content = new String(Files.readAllBytes(path), charset);
            content = content.replaceAll(key, newKey);
            Files.write(Paths.get(outFile), content.getBytes(charset));
        }catch (Exception e){
            LOGGER.error("Got error: " + e.getMessage());
        }
    }

    public static boolean unzipFileOnDownloadFolder(String fileNameZip, String downloadFolder) {
        String zipFilePath = convertDirectory(downloadFolder.concat(fileNameZip));
        String destDir = convertDirectory(downloadFolder);
        File dir = new File(destDir);
        if (!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while (ze != null) {
                String fileName = ze.getName();
                LOGGER.info("File name: " + fileName);
                File newFile = new File(destDir + File.separator + fileName);
                LOGGER.info("Unzipping to: " + newFile.getAbsolutePath());
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            fis.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean verifyOnlyFileOnZipDownloadFile(List<String> listFileContainName, String filePath) throws IOException {
        ZipFile sourceZipFile = new ZipFile(filePath);
        Enumeration e = sourceZipFile.entries();
        LOGGER.info("Size of Zip File: " + sourceZipFile.size());

        LOGGER.info("Trying to search " + listFileContainName.toString() + " in " + sourceZipFile.getName());
        if (listFileContainName.size() == sourceZipFile.size()) {
            sourceZipFile.close();
            for (String fileContainName : listFileContainName) {
                if (!searchFileOnZipDownloadFile(fileContainName, filePath))
                    return false;
            }
        } else
            return false;
        return true;
    }

    public static boolean searchFileOnZipDownloadFile(String fileContainName, String filePath) throws IOException {
        ZipFile sourceZipFile = new ZipFile(filePath);

        Enumeration e = sourceZipFile.entries();

        LOGGER.info("Trying to search " + fileContainName + " in " + sourceZipFile.getName());

        while (e.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) e.nextElement();
            LOGGER.info("entry.getName() = " + entry.getName());
            if (entry.getName().indexOf(fileContainName) != -1) {
                LOGGER.info("Found " + entry.getName());
                sourceZipFile.close();
                return true;
            }
        }

        LOGGER.info("File :" + fileContainName + " Not Found Inside Zip File: " + sourceZipFile.getName());
        sourceZipFile.close();
        return false;
    }

    public static void deleteAllFileExists(String pathLocation) {
        File file = new File(pathLocation);
        LOGGER.info("file: " + pathLocation);
        String[] myFiles;
        if (file.isDirectory()) {
            myFiles = file.list();
            for (String mfile : myFiles) {
                File myFile = new File(file, mfile);
                myFile.delete();
            }

        }
    }

    public static boolean checkFileExists(String pathLocation, boolean deleteExisted) {

        Path path = Paths.get(pathLocation);
        LOGGER.info("file: " + path);
        boolean result = false;
        try {
            if (Files.exists(path)) {
                result = true;
                if (deleteExisted) {
                    Files.delete(path);
                    if (Files.exists(path)) {
                        return false;
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return result;
    }

    public static String calculateMD5(String fileMD5) {
        String md5 = null;
        try {
            File file = new File(fileMD5);
            LOGGER.info("fileMD5 = " + fileMD5);
            MessageDigest md5Digest = MessageDigest.getInstance("MD5");
            md5 = getFileChecksum(md5Digest, file);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info("Unable to calculate MD5 file.");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return md5;

    }

    public static void attachFile(String commentFile, String folder) {
        try {
            String pathAttachLocation = "";
            pathAttachLocation = convertDirectory(folder);
            Thread.sleep(2000);
            StringSelection ss = new StringSelection(pathAttachLocation);
            StringSelection ss2 = new StringSelection(commentFile);
            LOGGER.info("Path is: " + pathAttachLocation);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, ss);
            Robot robot = new Robot();
            robot.delay(2);
            Thread.sleep(1000);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            Thread.sleep(1000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss2, ss2);
            Thread.sleep(1000);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            Thread.sleep(1000);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            Thread.sleep(4000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getValuePropertiesFile(String sFile, String sKey) {
        LOGGER.info("*** Read Configuration file ***");
        Properties prop = new Properties();
        String sValue = null;
        try {
            InputStream input = new FileInputStream(sFile);
            prop.load(input);
            sValue = prop.getProperty(sKey);
            LOGGER.info("*** Value from Properties file of Parameter: " + sKey + ": " + sValue);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOGGER.info("*** Can not find the properties file ***" + sValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sValue;
    }

    public static void setValuePropertiesFile(String sFile, String sKey, String sValue) {
        LOGGER.info("*** Read Configuration file ***");
        Properties prop = new Properties();
        try {
            FileInputStream fis = new FileInputStream(new File(sFile));
            prop.load(fis);
            fis.close();
            FileOutputStream fos = new FileOutputStream(new File(sFile));
            prop.setProperty(sKey, sValue);
            prop.store(fos, "Updating folder path");
            fos.close();
            LOGGER.info("*** Value from Properties file of Parameter: " + sKey + "be set: " + sValue);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getCSVFile(String fileName, String filePath){
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        String[] csvData = null;
        try {
            String csvFile = filePath + "/" + fileName + ".csv";
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                csvData = line.split(cvsSplitBy);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return csvData[0];
    }

    public static void verifyDownloadFileSuccessAndChecksum(String fileName, String folderDownload) {
        String concatUpload = convertDirectory(folderDownload.concat(fileName));
        String concatDownload = convertDirectory(folderDownload.concat(fileName));
        boolean fileExisted = checkFileExists(concatDownload, false);
        Assert.assertTrue(String.format("File '%s' should be existed.", fileName), fileExisted);
        if (fileExisted) {
            String checkMd5UploadFile = calculateMD5(concatUpload);
            LOGGER.info("md5 upload is: " + checkMd5UploadFile);
            String checkMd5DownloadFile = calculateMD5(concatDownload);
            LOGGER.info("md5 download is: " + checkMd5DownloadFile);
            Assert.assertEquals(checkMd5UploadFile, checkMd5DownloadFile,
                    "Checksum Download File and Upload File should be matched.");
        }
    }

    public static void verifyDownloadListFilesIsNotDownload(String fileName, String folderDownload) {
        LOGGER.info("Verify file " + fileName + " is not existed on computer");
        String concatDownload = convertDirectory(folderDownload.concat(fileName));
        boolean fileExisted = checkFileExists(concatDownload, false);
        Assert.assertFalse(String.format("File '%s' should be not existed.", fileName), fileExisted);
    }

    public static String readContextTextFile(String fileName){
        String data = "";
        try {
            data = new String(Files.readAllBytes(Paths.get(fileName)));
        }catch (Exception e){
            LOGGER.error("Can not get Text Data");
        }
        return data;
    }

    private static String getFileChecksum(MessageDigest digest, File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;
        while ((bytesCount = fis.read(byteArray)) != -1) {
            digest.update(byteArray, 0, bytesCount);
        }
        fis.close();
        byte[] bytes = digest.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public static void createDirectory(File destFolder) {
        if (destFolder.exists()) {
            deleteDirectory(destFolder);
            destFolder.mkdirs();
        } else if (destFolder.isDirectory()) {
            destFolder.mkdirs();
        }

    }

    public static void copyDirectory(File srcFolder, File destFolder) {
        createDirectory(destFolder);
        LOGGER.info("Directory copied from " + srcFolder + " to " + destFolder);
        if (srcFolder.isDirectory()) {
            if (!destFolder.exists()) {
                destFolder.mkdir();
            }

            String[] files = srcFolder.list();
            String[] var3 = files;
            int var4 = files.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String file = var3[var5];
                File srcFile = new File(srcFolder, file);
                File destFile = new File(destFolder, file);
                copyDirectory(srcFile, destFile);
            }
        } else {
            try {
                InputStream in = new FileInputStream(srcFolder);
                Throwable var38 = null;

                try {
                    OutputStream out = new FileOutputStream(destFolder);
                    Throwable var40 = null;

                    try {
                        byte[] buffer = new byte[1024];

                        int length;
                        while((length = in.read(buffer)) > 0) {
                            out.write(buffer, 0, length);
                        }

                        LOGGER.info("File copied from " + srcFolder + " to " + destFolder);
                    } catch (Throwable var32) {
                        var40 = var32;
                        throw var32;
                    } finally {
                        if (out != null) {
                            if (var40 != null) {
                                try {
                                    out.close();
                                } catch (Throwable var31) {
                                    var40.addSuppressed(var31);
                                }
                            } else {
                                out.close();
                            }
                        }

                    }
                } catch (Throwable var34) {
                    var38 = var34;
                    throw var34;
                } finally {
                    if (in != null) {
                        if (var38 != null) {
                            try {
                                in.close();
                            } catch (Throwable var30) {
                                var38.addSuppressed(var30);
                            }
                        } else {
                            in.close();
                        }
                    }

                }
            } catch (IOException var36) {
                LOGGER.error(String.valueOf(var36));
            }
        }

    }

    public static void deleteDirectory(File file) {
        if (file.isDirectory()) {
            if (file.list().length == 0) {
                file.delete();
                LOGGER.info("Directory is deleted : " + file.getAbsolutePath());
            } else {
                String[] files = file.list();
                String[] var2 = files;
                int var3 = files.length;

                for(int var4 = 0; var4 < var3; ++var4) {
                    String temp = var2[var4];
                    File fileDelete = new File(file, temp);
                    deleteDirectory(fileDelete);
                }

                if (file.list().length == 0) {
                    file.delete();
                    LOGGER.info("Directory is deleted: " + file.getAbsolutePath());
                }
            }
        } else {
            file.delete();
            LOGGER.info("File is deleted: " + file.getAbsolutePath());
        }

    }

    public static List<File> getAllFileWithExtension(String dir, String extension) {
        List<File> listCurrentFileInDir = getAllFileInFolder(dir);
        return !listCurrentFileInDir.isEmpty() ? listCurrentFileInDir.stream().filter((e) -> {
            return e.getAbsolutePath().endsWith(extension);
        }).collect(Collectors.toList()) : Collections.emptyList();
    }

    public static List<File> getAllFileInFolder(String dir) {
        try {
            Stream<Path> paths = Files.walk(Paths.get(dir));
            Throwable var2 = null;

            List var3;
            try {
                var3 = paths.filter((x$0) -> {
                    return Files.isRegularFile(x$0);
                }).map(Path::toFile).collect(Collectors.toList());
            } catch (Throwable var13) {
                var2 = var13;
                throw var13;
            } finally {
                if (paths != null) {
                    if (var2 != null) {
                        try {
                            paths.close();
                        } catch (Throwable var12) {
                            var2.addSuppressed(var12);
                        }
                    } else {
                        paths.close();
                    }
                }

            }

            return var3;
        } catch (IOException var15) {
            LOGGER.info(var15.getMessage());
            return Collections.emptyList();
        }
    }

    public static List<String> getAllFileNameInFileList(List<File> fileList) {
        return fileList.stream().filter(File::isFile).map(File::getName).collect(Collectors.toList());
    }

}
