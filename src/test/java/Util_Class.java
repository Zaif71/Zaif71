import java.util.HashMap;
import java.util.Map;

public class Util_Class {

    public static Map cookie() {

        Map<String, String> cookiesMap = new HashMap<>();
        cookiesMap.put("cookie1", "\n" +
                "GPS=1; VISITOR_INFO1_LIVE=bTv8preZjDA; VISITOR_PRIVACY_METADATA=CgJJThIEGgAgHw%3D%3D; YSC=hPsw1oRntdY");
        cookiesMap.put("cookie2", "value2");
        cookiesMap.put("cookie3", "value3");
        return cookiesMap;

    }
}
