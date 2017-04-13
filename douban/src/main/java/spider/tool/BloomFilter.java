package spider.tool;

import java.util.BitSet;

public class BloomFilter {
  private static int defaultSize = 2 << 10;
  private static int basic = defaultSize - 1;
  public volatile static BitSet bitSet=new BitSet(defaultSize);
  public  void add(String url) {
      if (url == null) {
          return;
      }
      int key1 = hashA(url);
      int key2 = hashB(url);
      int key3 = hashC(url);
      bitSet.set(key1);
      bitSet.set(key2);
      bitSet.set(key3);
  }

  public  boolean contains(String url) {
      if (url == null) {
          return true;
      }
      int key1 = hashA(url);
      int key2 = hashB(url);
      int key3 = hashC(url);
      if (bitSet.get(key1) && bitSet.get(key2) && bitSet.get(key3)) {
          return true;
      }
      return false;
  }
  private static int check(int speed) {
      return basic & speed;
  }
  public  boolean ifNotContainsSet(String url) {
      if (url == null) {
          return true;
      }
      int key1 = hashA(url);
      int key2 = hashB(url);
      int key3 = hashC(url);
      if (bitSet.get(key1) && bitSet.get(key2) && bitSet.get(key3)) {
          return true;
      }
      bitSet.set(key1);
      bitSet.set(key2);
      bitSet.set(key3);
      return false;
  }
  private  int hashA(String url) {
      int speed = 0;
      for (int i = 0; i < url.length(); i++) {
          speed = 13 * speed + url.charAt(i);
      }
      return check(speed);
  }
  private  int hashB(String url) {
      int speed = 0;
      for (int i = 0; i < url.length(); i++) {
          speed = 23 * speed + url.charAt(i);
      }
      return check(speed);
  }
  private  int hashC(String url) {
      int speed = 0;
      for (int i = 0; i < url.length(); i++) {
          speed = 34 * speed + url.charAt(i);
      }
      return check(speed);
  }
}