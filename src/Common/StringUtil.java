package Common;
/**
 * 各种格式化转换
 * @author ctk
 *
 */
import java.text.SimpleDateFormat;
import java.util.Date;
public class StringUtil {
	public static SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
	public static String millsToString(long mills){
		Date date = new Date(mills);
		return format1.format(date).toString();
	}

}
