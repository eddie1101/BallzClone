

import java.io.*;

public class HighScoreHandler {

    public static String FILE_PATH;
    public static final int BUFFER_SIZE = 10;

    private File file;

    private  FileInputStream fin;
    private  FileOutputStream fout;

    public HighScoreHandler(String path) {
        FILE_PATH = path;
        file = new File(FILE_PATH);
    }

    public void closeStreams() {
        try {
            if(fin != null)
                fin.close();
            if(fout != null)
                fout.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void ensureHighScoreExists() {
        try {
            if (!file.exists()) {
                fout = new FileOutputStream(file);
                fout.write('0');
            } else {
                fin = new FileInputStream(file);
                if(fin.read() == -1) {
                    fout = new FileOutputStream(file);
                    fout.write('0');
                }
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } finally {
            closeStreams();
        }
    }

    public int getHighScore() {

        int read, i = 0;
        char[] buffer = new char[BUFFER_SIZE];
        try {
            fin = new FileInputStream(file);
            while ((read = fin.read()) != -1) {
                buffer[i++] = (char) read;
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } finally {
            closeStreams();
        }

        StringBuilder str = new StringBuilder();
        for(char c: buffer) {
            if((int) c != 0)
                str.append(c);
        }

        try {
            return Integer.parseInt(str.toString());
        } catch(NumberFormatException nfe) {
            System.err.println("Dont fuck with the high score file you prude");
            return -1;
        }

    }

    public void putHighScore(int score) {

        char[] buffer = String.valueOf(score).toCharArray();

        try {
            fout = new FileOutputStream(file);
            for(char c: buffer) {
                fout.write(c);
            }
            fout.flush();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            closeStreams();
        }

    }

}
