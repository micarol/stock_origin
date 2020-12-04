import java.util.Random;

import com.micarol.stock.util.StringUtil;

import redis.clients.jedis.Jedis;

public class JedisTest {

	public static void main(String[] args) {
//        Jedis con = new Jedis("10.0.0.40", 6479);
//        con.auth("unidsp");
//        String s = con.hget("asdsa", "aaa");
//        String t = (String)s;
//        System.out.println(t);
//        System.out.println(t==null);
//        System.out.println("null".equals(t));
//		stringTest();
		hashTest();
    }
	
	public static void stringTest() {
		Jedis con = new Jedis("10.0.0.40", 6479);
        con.auth("unidsp");
		int limit = 100000;
		long uid = 2582600000l;
		for(int i=0;i<limit;i++) {
			String uidMd5 = StringUtil.getMD5(uid+i+"");
			if(i%10000==0) {
				System.out.println("i="+i);
				System.out.println(uidMd5);
			}
			con.set(uidMd5, "1");
		}
		System.out.println("over");	
	}
	
	public static void hashTest() {
		Jedis con = new Jedis("10.0.0.40", 6479);
        con.auth("unidsp");
		int limit = 100000;
		long uid = 2582600000l;
		for(int i=0;i<limit;i++) {
			String uidMd5 = StringUtil.getMD5(uid+i*new Random().nextInt(1000)+"");
			if(i%10000==0) {
				System.out.println("i="+i);
				System.out.println(uidMd5);
			}
			con.hset(uidMd5.substring(0, 6), uidMd5.substring(6), "1");
		}
		System.out.println("over");	
	}
}
