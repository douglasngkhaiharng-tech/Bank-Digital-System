public class StringUtil {
    
    public static int getDisplayWidth(String str) {
        if (str == null) return 0;
        int width = 0;
        for (char c : str.toCharArray()) {
            if (c >= 0x4E00 && c <= 0x9FA5) {
                width += 2;
            } else {
                width += 1;
            }
        }
        return width;
    }
    
    public static String padRight(String str, int width) {
        if (str == null) str = "";
        int currentWidth = getDisplayWidth(str);
        int padding = width - currentWidth;
        if (padding <= 0) return str;
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < padding; i++) {
            sb.append(' ');
        }
        return sb.toString();
    }
    
    public static String padLeft(String str, int width) {
        if (str == null) str = "";
        int currentWidth = getDisplayWidth(str);
        int padding = width - currentWidth;
        if (padding <= 0) return str;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            sb.append(' ');
        }
        sb.append(str);
        return sb.toString();
    }
    
    public static String center(String str, int width) {
        if (str == null) str = "";
        int strWidth = getDisplayWidth(str);
        int padding = (width - strWidth) / 2;
        if (padding <= 0) return str;
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            sb.append(' ');
        }
        sb.append(str);
        while (getDisplayWidth(sb.toString()) < width) {
            sb.append(' ');
        }
        return sb.toString();
    }
    
    public static String createLine(char c, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}