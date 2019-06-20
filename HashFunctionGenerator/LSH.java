public interface LSH {
  int hash(byte[] hash64);
}
class Hash0 {
  public int hash0(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[2] >>> 4) & 1 ) != 0 ));
bs.set(1, (((hash64[1] >>> 3) & 1 ) != 0 ));
bs.set(2, (((hash64[7] >>> 2) & 1 ) != 0 ));
bs.set(3, (((hash64[2] >>> 1) & 1 ) != 0 ));
bs.set(4, (((hash64[1] >>> 0) & 1 ) != 0 ));
bs.set(5, (((hash64[1] >>> 4) & 1 ) != 0 ));
bs.set(6, (((hash64[2] >>> 0) & 1 ) != 0 ));
bs.set(7, (((hash64[0] >>> 0) & 1 ) != 0 ));
bs.set(8, (((hash64[1] >>> 7) & 1 ) != 0 ));
bs.set(9, (((hash64[3] >>> 6) & 1 ) != 0 ));
bs.set(10, (((hash64[3] >>> 7) & 1 ) != 0 ));
bs.set(11, (((hash64[6] >>> 1) & 1 ) != 0 ));
bs.set(12, (((hash64[5] >>> 5) & 1 ) != 0 ));
bs.set(13, (((hash64[0] >>> 6) & 1 ) != 0 ));
bs.set(14, (((hash64[3] >>> 0) & 1 ) != 0 ));
bs.set(15, (((hash64[4] >>> 5) & 1 ) != 0 ));
bs.set(16, (((hash64[3] >>> 3) & 1 ) != 0 ));
bs.set(17, (((hash64[6] >>> 3) & 1 ) != 0 ));
bs.set(18, (((hash64[4] >>> 3) & 1 ) != 0 ));
bs.set(19, (((hash64[5] >>> 0) & 1 ) != 0 ));
 return bs.hashCode();}
}


class Hash1 {
  public int hash1(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[4] >>> 0) & 1 ) != 0 ));
bs.set(1, (((hash64[1] >>> 2) & 1 ) != 0 ));
bs.set(2, (((hash64[0] >>> 1) & 1 ) != 0 ));
bs.set(3, (((hash64[0] >>> 2) & 1 ) != 0 ));
bs.set(4, (((hash64[5] >>> 1) & 1 ) != 0 ));
bs.set(5, (((hash64[6] >>> 5) & 1 ) != 0 ));
bs.set(6, (((hash64[3] >>> 1) & 1 ) != 0 ));
bs.set(7, (((hash64[2] >>> 6) & 1 ) != 0 ));
bs.set(8, (((hash64[2] >>> 1) & 1 ) != 0 ));
bs.set(9, (((hash64[3] >>> 7) & 1 ) != 0 ));
bs.set(10, (((hash64[5] >>> 0) & 1 ) != 0 ));
bs.set(11, (((hash64[6] >>> 7) & 1 ) != 0 ));
bs.set(12, (((hash64[0] >>> 0) & 1 ) != 0 ));
bs.set(13, (((hash64[6] >>> 0) & 1 ) != 0 ));
bs.set(14, (((hash64[3] >>> 5) & 1 ) != 0 ));
bs.set(15, (((hash64[5] >>> 3) & 1 ) != 0 ));
bs.set(16, (((hash64[2] >>> 5) & 1 ) != 0 ));
bs.set(17, (((hash64[2] >>> 2) & 1 ) != 0 ));
bs.set(18, (((hash64[4] >>> 1) & 1 ) != 0 ));
bs.set(19, (((hash64[5] >>> 2) & 1 ) != 0 ));
 return bs.hashCode();}
}


class Hash2 {
  public int hash2(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[6] >>> 3) & 1 ) != 0 ));
bs.set(1, (((hash64[1] >>> 7) & 1 ) != 0 ));
bs.set(2, (((hash64[3] >>> 2) & 1 ) != 0 ));
bs.set(3, (((hash64[7] >>> 4) & 1 ) != 0 ));
bs.set(4, (((hash64[1] >>> 2) & 1 ) != 0 ));
bs.set(5, (((hash64[6] >>> 2) & 1 ) != 0 ));
bs.set(6, (((hash64[0] >>> 4) & 1 ) != 0 ));
bs.set(7, (((hash64[3] >>> 5) & 1 ) != 0 ));
bs.set(8, (((hash64[3] >>> 0) & 1 ) != 0 ));
bs.set(9, (((hash64[3] >>> 3) & 1 ) != 0 ));
bs.set(10, (((hash64[6] >>> 4) & 1 ) != 0 ));
bs.set(11, (((hash64[6] >>> 1) & 1 ) != 0 ));
bs.set(12, (((hash64[4] >>> 2) & 1 ) != 0 ));
bs.set(13, (((hash64[5] >>> 2) & 1 ) != 0 ));
bs.set(14, (((hash64[7] >>> 3) & 1 ) != 0 ));
bs.set(15, (((hash64[7] >>> 2) & 1 ) != 0 ));
bs.set(16, (((hash64[6] >>> 7) & 1 ) != 0 ));
bs.set(17, (((hash64[4] >>> 0) & 1 ) != 0 ));
bs.set(18, (((hash64[2] >>> 2) & 1 ) != 0 ));
bs.set(19, (((hash64[6] >>> 0) & 1 ) != 0 ));
 return bs.hashCode();}
}


class Hash3 {
  public int hash3(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[4] >>> 3) & 1 ) != 0 ));
bs.set(1, (((hash64[6] >>> 6) & 1 ) != 0 ));
bs.set(2, (((hash64[7] >>> 0) & 1 ) != 0 ));
bs.set(3, (((hash64[5] >>> 6) & 1 ) != 0 ));
bs.set(4, (((hash64[5] >>> 3) & 1 ) != 0 ));
bs.set(5, (((hash64[0] >>> 1) & 1 ) != 0 ));
bs.set(6, (((hash64[3] >>> 1) & 1 ) != 0 ));
bs.set(7, (((hash64[1] >>> 0) & 1 ) != 0 ));
bs.set(8, (((hash64[6] >>> 1) & 1 ) != 0 ));
bs.set(9, (((hash64[1] >>> 4) & 1 ) != 0 ));
bs.set(10, (((hash64[2] >>> 4) & 1 ) != 0 ));
bs.set(11, (((hash64[7] >>> 1) & 1 ) != 0 ));
bs.set(12, (((hash64[1] >>> 1) & 1 ) != 0 ));
bs.set(13, (((hash64[0] >>> 2) & 1 ) != 0 ));
bs.set(14, (((hash64[3] >>> 0) & 1 ) != 0 ));
bs.set(15, (((hash64[2] >>> 2) & 1 ) != 0 ));
bs.set(16, (((hash64[1] >>> 5) & 1 ) != 0 ));
bs.set(17, (((hash64[2] >>> 6) & 1 ) != 0 ));
bs.set(18, (((hash64[2] >>> 5) & 1 ) != 0 ));
bs.set(19, (((hash64[5] >>> 1) & 1 ) != 0 ));
 return bs.hashCode();}
}


class Hash4 {
  public int hash4(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[1] >>> 3) & 1 ) != 0 ));
bs.set(1, (((hash64[5] >>> 3) & 1 ) != 0 ));
bs.set(2, (((hash64[5] >>> 2) & 1 ) != 0 ));
bs.set(3, (((hash64[5] >>> 6) & 1 ) != 0 ));
bs.set(4, (((hash64[6] >>> 7) & 1 ) != 0 ));
bs.set(5, (((hash64[3] >>> 1) & 1 ) != 0 ));
bs.set(6, (((hash64[1] >>> 6) & 1 ) != 0 ));
bs.set(7, (((hash64[6] >>> 2) & 1 ) != 0 ));
bs.set(8, (((hash64[2] >>> 4) & 1 ) != 0 ));
bs.set(9, (((hash64[6] >>> 1) & 1 ) != 0 ));
bs.set(10, (((hash64[4] >>> 0) & 1 ) != 0 ));
bs.set(11, (((hash64[7] >>> 2) & 1 ) != 0 ));
bs.set(12, (((hash64[6] >>> 4) & 1 ) != 0 ));
bs.set(13, (((hash64[1] >>> 4) & 1 ) != 0 ));
bs.set(14, (((hash64[0] >>> 2) & 1 ) != 0 ));
bs.set(15, (((hash64[7] >>> 0) & 1 ) != 0 ));
bs.set(16, (((hash64[7] >>> 4) & 1 ) != 0 ));
bs.set(17, (((hash64[1] >>> 1) & 1 ) != 0 ));
bs.set(18, (((hash64[1] >>> 0) & 1 ) != 0 ));
bs.set(19, (((hash64[4] >>> 1) & 1 ) != 0 ));
 return bs.hashCode();}
}


class Hash5 {
  public int hash5(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[2] >>> 5) & 1 ) != 0 ));
bs.set(1, (((hash64[2] >>> 0) & 1 ) != 0 ));
bs.set(2, (((hash64[7] >>> 1) & 1 ) != 0 ));
bs.set(3, (((hash64[1] >>> 2) & 1 ) != 0 ));
bs.set(4, (((hash64[2] >>> 2) & 1 ) != 0 ));
bs.set(5, (((hash64[4] >>> 7) & 1 ) != 0 ));
bs.set(6, (((hash64[5] >>> 1) & 1 ) != 0 ));
bs.set(7, (((hash64[4] >>> 5) & 1 ) != 0 ));
bs.set(8, (((hash64[2] >>> 3) & 1 ) != 0 ));
bs.set(9, (((hash64[5] >>> 3) & 1 ) != 0 ));
bs.set(10, (((hash64[4] >>> 0) & 1 ) != 0 ));
bs.set(11, (((hash64[2] >>> 7) & 1 ) != 0 ));
bs.set(12, (((hash64[3] >>> 7) & 1 ) != 0 ));
bs.set(13, (((hash64[1] >>> 0) & 1 ) != 0 ));
bs.set(14, (((hash64[6] >>> 6) & 1 ) != 0 ));
bs.set(15, (((hash64[5] >>> 0) & 1 ) != 0 ));
bs.set(16, (((hash64[3] >>> 0) & 1 ) != 0 ));
bs.set(17, (((hash64[5] >>> 4) & 1 ) != 0 ));
bs.set(18, (((hash64[1] >>> 1) & 1 ) != 0 ));
bs.set(19, (((hash64[7] >>> 0) & 1 ) != 0 ));
 return bs.hashCode();}
}


class Hash6 {
  public int hash6(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[7] >>> 6) & 1 ) != 0 ));
bs.set(1, (((hash64[4] >>> 4) & 1 ) != 0 ));
bs.set(2, (((hash64[3] >>> 6) & 1 ) != 0 ));
bs.set(3, (((hash64[2] >>> 1) & 1 ) != 0 ));
bs.set(4, (((hash64[3] >>> 0) & 1 ) != 0 ));
bs.set(5, (((hash64[5] >>> 7) & 1 ) != 0 ));
bs.set(6, (((hash64[5] >>> 0) & 1 ) != 0 ));
bs.set(7, (((hash64[7] >>> 1) & 1 ) != 0 ));
bs.set(8, (((hash64[1] >>> 1) & 1 ) != 0 ));
bs.set(9, (((hash64[7] >>> 5) & 1 ) != 0 ));
bs.set(10, (((hash64[6] >>> 7) & 1 ) != 0 ));
bs.set(11, (((hash64[0] >>> 3) & 1 ) != 0 ));
bs.set(12, (((hash64[7] >>> 3) & 1 ) != 0 ));
bs.set(13, (((hash64[2] >>> 5) & 1 ) != 0 ));
bs.set(14, (((hash64[0] >>> 2) & 1 ) != 0 ));
bs.set(15, (((hash64[6] >>> 3) & 1 ) != 0 ));
bs.set(16, (((hash64[6] >>> 2) & 1 ) != 0 ));
bs.set(17, (((hash64[6] >>> 0) & 1 ) != 0 ));
bs.set(18, (((hash64[5] >>> 2) & 1 ) != 0 ));
bs.set(19, (((hash64[2] >>> 6) & 1 ) != 0 ));
 return bs.hashCode();}
}


class Hash7 {
  public int hash7(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[5] >>> 5) & 1 ) != 0 ));
bs.set(1, (((hash64[3] >>> 2) & 1 ) != 0 ));
bs.set(2, (((hash64[0] >>> 2) & 1 ) != 0 ));
bs.set(3, (((hash64[7] >>> 5) & 1 ) != 0 ));
bs.set(4, (((hash64[1] >>> 6) & 1 ) != 0 ));
bs.set(5, (((hash64[0] >>> 3) & 1 ) != 0 ));
bs.set(6, (((hash64[6] >>> 4) & 1 ) != 0 ));
bs.set(7, (((hash64[1] >>> 3) & 1 ) != 0 ));
bs.set(8, (((hash64[4] >>> 7) & 1 ) != 0 ));
bs.set(9, (((hash64[3] >>> 1) & 1 ) != 0 ));
bs.set(10, (((hash64[5] >>> 0) & 1 ) != 0 ));
bs.set(11, (((hash64[1] >>> 4) & 1 ) != 0 ));
bs.set(12, (((hash64[6] >>> 7) & 1 ) != 0 ));
bs.set(13, (((hash64[2] >>> 5) & 1 ) != 0 ));
bs.set(14, (((hash64[1] >>> 7) & 1 ) != 0 ));
bs.set(15, (((hash64[2] >>> 7) & 1 ) != 0 ));
bs.set(16, (((hash64[4] >>> 3) & 1 ) != 0 ));
bs.set(17, (((hash64[3] >>> 4) & 1 ) != 0 ));
bs.set(18, (((hash64[6] >>> 1) & 1 ) != 0 ));
bs.set(19, (((hash64[1] >>> 5) & 1 ) != 0 ));
 return bs.hashCode();}
}


class Hash8 {
  public int hash8(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[4] >>> 1) & 1 ) != 0 ));
bs.set(1, (((hash64[4] >>> 4) & 1 ) != 0 ));
bs.set(2, (((hash64[0] >>> 0) & 1 ) != 0 ));
bs.set(3, (((hash64[3] >>> 4) & 1 ) != 0 ));
bs.set(4, (((hash64[4] >>> 7) & 1 ) != 0 ));
bs.set(5, (((hash64[7] >>> 0) & 1 ) != 0 ));
bs.set(6, (((hash64[7] >>> 6) & 1 ) != 0 ));
bs.set(7, (((hash64[5] >>> 4) & 1 ) != 0 ));
bs.set(8, (((hash64[6] >>> 3) & 1 ) != 0 ));
bs.set(9, (((hash64[7] >>> 5) & 1 ) != 0 ));
bs.set(10, (((hash64[2] >>> 5) & 1 ) != 0 ));
bs.set(11, (((hash64[6] >>> 2) & 1 ) != 0 ));
bs.set(12, (((hash64[2] >>> 1) & 1 ) != 0 ));
bs.set(13, (((hash64[3] >>> 1) & 1 ) != 0 ));
bs.set(14, (((hash64[0] >>> 7) & 1 ) != 0 ));
bs.set(15, (((hash64[1] >>> 6) & 1 ) != 0 ));
bs.set(16, (((hash64[6] >>> 7) & 1 ) != 0 ));
bs.set(17, (((hash64[1] >>> 0) & 1 ) != 0 ));
bs.set(18, (((hash64[0] >>> 2) & 1 ) != 0 ));
bs.set(19, (((hash64[0] >>> 5) & 1 ) != 0 ));
 return bs.hashCode();}
}


class Hash9 {
  public int hash9(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[5] >>> 0) & 1 ) != 0 ));
bs.set(1, (((hash64[3] >>> 4) & 1 ) != 0 ));
bs.set(2, (((hash64[2] >>> 3) & 1 ) != 0 ));
bs.set(3, (((hash64[1] >>> 7) & 1 ) != 0 ));
bs.set(4, (((hash64[4] >>> 7) & 1 ) != 0 ));
bs.set(5, (((hash64[6] >>> 3) & 1 ) != 0 ));
bs.set(6, (((hash64[0] >>> 1) & 1 ) != 0 ));
bs.set(7, (((hash64[5] >>> 3) & 1 ) != 0 ));
bs.set(8, (((hash64[7] >>> 0) & 1 ) != 0 ));
bs.set(9, (((hash64[0] >>> 0) & 1 ) != 0 ));
bs.set(10, (((hash64[0] >>> 2) & 1 ) != 0 ));
bs.set(11, (((hash64[6] >>> 1) & 1 ) != 0 ));
bs.set(12, (((hash64[2] >>> 4) & 1 ) != 0 ));
bs.set(13, (((hash64[1] >>> 2) & 1 ) != 0 ));
bs.set(14, (((hash64[6] >>> 6) & 1 ) != 0 ));
bs.set(15, (((hash64[3] >>> 5) & 1 ) != 0 ));
bs.set(16, (((hash64[6] >>> 4) & 1 ) != 0 ));
bs.set(17, (((hash64[3] >>> 1) & 1 ) != 0 ));
bs.set(18, (((hash64[2] >>> 5) & 1 ) != 0 ));
bs.set(19, (((hash64[7] >>> 2) & 1 ) != 0 ));
 return bs.hashCode();}
}


class Hash10 {
  public int hash10(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[0] >>> 3) & 1 ) != 0 ));
bs.set(1, (((hash64[4] >>> 1) & 1 ) != 0 ));
bs.set(2, (((hash64[6] >>> 5) & 1 ) != 0 ));
bs.set(3, (((hash64[1] >>> 4) & 1 ) != 0 ));
bs.set(4, (((hash64[3] >>> 2) & 1 ) != 0 ));
bs.set(5, (((hash64[5] >>> 5) & 1 ) != 0 ));
bs.set(6, (((hash64[0] >>> 1) & 1 ) != 0 ));
bs.set(7, (((hash64[6] >>> 0) & 1 ) != 0 ));
bs.set(8, (((hash64[7] >>> 4) & 1 ) != 0 ));
bs.set(9, (((hash64[5] >>> 0) & 1 ) != 0 ));
bs.set(10, (((hash64[1] >>> 5) & 1 ) != 0 ));
bs.set(11, (((hash64[2] >>> 2) & 1 ) != 0 ));
bs.set(12, (((hash64[7] >>> 7) & 1 ) != 0 ));
bs.set(13, (((hash64[6] >>> 6) & 1 ) != 0 ));
bs.set(14, (((hash64[1] >>> 2) & 1 ) != 0 ));
bs.set(15, (((hash64[0] >>> 2) & 1 ) != 0 ));
bs.set(16, (((hash64[6] >>> 1) & 1 ) != 0 ));
bs.set(17, (((hash64[3] >>> 3) & 1 ) != 0 ));
bs.set(18, (((hash64[7] >>> 1) & 1 ) != 0 ));
bs.set(19, (((hash64[4] >>> 6) & 1 ) != 0 ));
 return bs.hashCode();}
}


class Hash11 {
  public int hash11(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[0] >>> 0) & 1 ) != 0 ));
bs.set(1, (((hash64[6] >>> 7) & 1 ) != 0 ));
bs.set(2, (((hash64[2] >>> 7) & 1 ) != 0 ));
bs.set(3, (((hash64[1] >>> 7) & 1 ) != 0 ));
bs.set(4, (((hash64[5] >>> 4) & 1 ) != 0 ));
bs.set(5, (((hash64[5] >>> 1) & 1 ) != 0 ));
bs.set(6, (((hash64[7] >>> 4) & 1 ) != 0 ));
bs.set(7, (((hash64[6] >>> 5) & 1 ) != 0 ));
bs.set(8, (((hash64[5] >>> 5) & 1 ) != 0 ));
bs.set(9, (((hash64[2] >>> 4) & 1 ) != 0 ));
bs.set(10, (((hash64[2] >>> 2) & 1 ) != 0 ));
bs.set(11, (((hash64[1] >>> 3) & 1 ) != 0 ));
bs.set(12, (((hash64[3] >>> 3) & 1 ) != 0 ));
bs.set(13, (((hash64[2] >>> 1) & 1 ) != 0 ));
bs.set(14, (((hash64[5] >>> 0) & 1 ) != 0 ));
bs.set(15, (((hash64[5] >>> 2) & 1 ) != 0 ));
bs.set(16, (((hash64[7] >>> 3) & 1 ) != 0 ));
bs.set(17, (((hash64[6] >>> 6) & 1 ) != 0 ));
bs.set(18, (((hash64[4] >>> 7) & 1 ) != 0 ));
bs.set(19, (((hash64[3] >>> 2) & 1 ) != 0 ));
 return bs.hashCode();}
}


class Hash12 {
  public int hash12(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[1] >>> 3) & 1 ) != 0 ));
bs.set(1, (((hash64[0] >>> 7) & 1 ) != 0 ));
bs.set(2, (((hash64[5] >>> 0) & 1 ) != 0 ));
bs.set(3, (((hash64[6] >>> 1) & 1 ) != 0 ));
bs.set(4, (((hash64[5] >>> 1) & 1 ) != 0 ));
bs.set(5, (((hash64[4] >>> 4) & 1 ) != 0 ));
bs.set(6, (((hash64[2] >>> 1) & 1 ) != 0 ));
bs.set(7, (((hash64[4] >>> 1) & 1 ) != 0 ));
bs.set(8, (((hash64[4] >>> 5) & 1 ) != 0 ));
bs.set(9, (((hash64[6] >>> 7) & 1 ) != 0 ));
bs.set(10, (((hash64[3] >>> 2) & 1 ) != 0 ));
bs.set(11, (((hash64[3] >>> 7) & 1 ) != 0 ));
bs.set(12, (((hash64[3] >>> 1) & 1 ) != 0 ));
bs.set(13, (((hash64[1] >>> 0) & 1 ) != 0 ));
bs.set(14, (((hash64[7] >>> 0) & 1 ) != 0 ));
bs.set(15, (((hash64[4] >>> 7) & 1 ) != 0 ));
bs.set(16, (((hash64[3] >>> 4) & 1 ) != 0 ));
bs.set(17, (((hash64[5] >>> 3) & 1 ) != 0 ));
bs.set(18, (((hash64[4] >>> 6) & 1 ) != 0 ));
bs.set(19, (((hash64[2] >>> 6) & 1 ) != 0 ));
 return bs.hashCode();}
}


class Hash13 {
  public int hash13(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[0] >>> 7) & 1 ) != 0 ));
bs.set(1, (((hash64[2] >>> 5) & 1 ) != 0 ));
bs.set(2, (((hash64[6] >>> 5) & 1 ) != 0 ));
bs.set(3, (((hash64[2] >>> 0) & 1 ) != 0 ));
bs.set(4, (((hash64[7] >>> 7) & 1 ) != 0 ));
bs.set(5, (((hash64[1] >>> 6) & 1 ) != 0 ));
bs.set(6, (((hash64[6] >>> 6) & 1 ) != 0 ));
bs.set(7, (((hash64[4] >>> 3) & 1 ) != 0 ));
bs.set(8, (((hash64[2] >>> 3) & 1 ) != 0 ));
bs.set(9, (((hash64[4] >>> 1) & 1 ) != 0 ));
bs.set(10, (((hash64[4] >>> 6) & 1 ) != 0 ));
bs.set(11, (((hash64[0] >>> 3) & 1 ) != 0 ));
bs.set(12, (((hash64[3] >>> 4) & 1 ) != 0 ));
bs.set(13, (((hash64[4] >>> 0) & 1 ) != 0 ));
bs.set(14, (((hash64[1] >>> 0) & 1 ) != 0 ));
bs.set(15, (((hash64[5] >>> 0) & 1 ) != 0 ));
bs.set(16, (((hash64[1] >>> 3) & 1 ) != 0 ));
bs.set(17, (((hash64[7] >>> 2) & 1 ) != 0 ));
bs.set(18, (((hash64[0] >>> 0) & 1 ) != 0 ));
bs.set(19, (((hash64[3] >>> 7) & 1 ) != 0 ));
 return bs.hashCode();}
}


class Hash14 {
  public int hash14(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[2] >>> 7) & 1 ) != 0 ));
bs.set(1, (((hash64[3] >>> 1) & 1 ) != 0 ));
bs.set(2, (((hash64[4] >>> 2) & 1 ) != 0 ));
bs.set(3, (((hash64[7] >>> 0) & 1 ) != 0 ));
bs.set(4, (((hash64[2] >>> 1) & 1 ) != 0 ));
bs.set(5, (((hash64[1] >>> 6) & 1 ) != 0 ));
bs.set(6, (((hash64[7] >>> 5) & 1 ) != 0 ));
bs.set(7, (((hash64[3] >>> 6) & 1 ) != 0 ));
bs.set(8, (((hash64[6] >>> 2) & 1 ) != 0 ));
bs.set(9, (((hash64[3] >>> 3) & 1 ) != 0 ));
bs.set(10, (((hash64[4] >>> 6) & 1 ) != 0 ));
bs.set(11, (((hash64[6] >>> 7) & 1 ) != 0 ));
bs.set(12, (((hash64[0] >>> 7) & 1 ) != 0 ));
bs.set(13, (((hash64[4] >>> 5) & 1 ) != 0 ));
bs.set(14, (((hash64[2] >>> 4) & 1 ) != 0 ));
bs.set(15, (((hash64[7] >>> 4) & 1 ) != 0 ));
bs.set(16, (((hash64[0] >>> 0) & 1 ) != 0 ));
bs.set(17, (((hash64[1] >>> 4) & 1 ) != 0 ));
bs.set(18, (((hash64[4] >>> 0) & 1 ) != 0 ));
bs.set(19, (((hash64[7] >>> 1) & 1 ) != 0 ));
 return bs.hashCode();}
}


class Hash15 {
  public int hash15(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[5] >>> 2) & 1 ) != 0 ));
bs.set(1, (((hash64[3] >>> 5) & 1 ) != 0 ));
bs.set(2, (((hash64[2] >>> 5) & 1 ) != 0 ));
bs.set(3, (((hash64[5] >>> 7) & 1 ) != 0 ));
bs.set(4, (((hash64[0] >>> 0) & 1 ) != 0 ));
bs.set(5, (((hash64[6] >>> 4) & 1 ) != 0 ));
bs.set(6, (((hash64[5] >>> 6) & 1 ) != 0 ));
bs.set(7, (((hash64[3] >>> 2) & 1 ) != 0 ));
bs.set(8, (((hash64[0] >>> 1) & 1 ) != 0 ));
bs.set(9, (((hash64[1] >>> 7) & 1 ) != 0 ));
bs.set(10, (((hash64[6] >>> 0) & 1 ) != 0 ));
bs.set(11, (((hash64[5] >>> 1) & 1 ) != 0 ));
bs.set(12, (((hash64[5] >>> 5) & 1 ) != 0 ));
bs.set(13, (((hash64[5] >>> 3) & 1 ) != 0 ));
bs.set(14, (((hash64[3] >>> 0) & 1 ) != 0 ));
bs.set(15, (((hash64[3] >>> 7) & 1 ) != 0 ));
bs.set(16, (((hash64[6] >>> 2) & 1 ) != 0 ));
bs.set(17, (((hash64[6] >>> 5) & 1 ) != 0 ));
bs.set(18, (((hash64[0] >>> 2) & 1 ) != 0 ));
bs.set(19, (((hash64[5] >>> 4) & 1 ) != 0 ));
 return bs.hashCode();}
}


class Hash16 {
  public int hash16(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[0] >>> 6) & 1 ) != 0 ));
bs.set(1, (((hash64[3] >>> 1) & 1 ) != 0 ));
bs.set(2, (((hash64[6] >>> 0) & 1 ) != 0 ));
bs.set(3, (((hash64[3] >>> 6) & 1 ) != 0 ));
bs.set(4, (((hash64[1] >>> 5) & 1 ) != 0 ));
bs.set(5, (((hash64[1] >>> 2) & 1 ) != 0 ));
bs.set(6, (((hash64[1] >>> 4) & 1 ) != 0 ));
bs.set(7, (((hash64[7] >>> 5) & 1 ) != 0 ));
bs.set(8, (((hash64[3] >>> 4) & 1 ) != 0 ));
bs.set(9, (((hash64[5] >>> 2) & 1 ) != 0 ));
bs.set(10, (((hash64[7] >>> 1) & 1 ) != 0 ));
bs.set(11, (((hash64[4] >>> 6) & 1 ) != 0 ));
bs.set(12, (((hash64[2] >>> 1) & 1 ) != 0 ));
bs.set(13, (((hash64[6] >>> 6) & 1 ) != 0 ));
bs.set(14, (((hash64[4] >>> 2) & 1 ) != 0 ));
bs.set(15, (((hash64[4] >>> 5) & 1 ) != 0 ));
bs.set(16, (((hash64[0] >>> 3) & 1 ) != 0 ));
bs.set(17, (((hash64[1] >>> 1) & 1 ) != 0 ));
bs.set(18, (((hash64[6] >>> 4) & 1 ) != 0 ));
bs.set(19, (((hash64[7] >>> 3) & 1 ) != 0 ));
 return bs.hashCode();}
}


class Hash17 {
  public int hash17(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[3] >>> 2) & 1 ) != 0 ));
bs.set(1, (((hash64[1] >>> 6) & 1 ) != 0 ));
bs.set(2, (((hash64[0] >>> 6) & 1 ) != 0 ));
bs.set(3, (((hash64[5] >>> 1) & 1 ) != 0 ));
bs.set(4, (((hash64[7] >>> 1) & 1 ) != 0 ));
bs.set(5, (((hash64[7] >>> 2) & 1 ) != 0 ));
bs.set(6, (((hash64[4] >>> 7) & 1 ) != 0 ));
bs.set(7, (((hash64[5] >>> 0) & 1 ) != 0 ));
bs.set(8, (((hash64[2] >>> 0) & 1 ) != 0 ));
bs.set(9, (((hash64[2] >>> 4) & 1 ) != 0 ));
bs.set(10, (((hash64[6] >>> 5) & 1 ) != 0 ));
bs.set(11, (((hash64[6] >>> 3) & 1 ) != 0 ));
bs.set(12, (((hash64[5] >>> 2) & 1 ) != 0 ));
bs.set(13, (((hash64[5] >>> 5) & 1 ) != 0 ));
bs.set(14, (((hash64[2] >>> 1) & 1 ) != 0 ));
bs.set(15, (((hash64[1] >>> 2) & 1 ) != 0 ));
bs.set(16, (((hash64[2] >>> 6) & 1 ) != 0 ));
bs.set(17, (((hash64[4] >>> 4) & 1 ) != 0 ));
bs.set(18, (((hash64[0] >>> 0) & 1 ) != 0 ));
bs.set(19, (((hash64[4] >>> 3) & 1 ) != 0 ));
 return bs.hashCode();}
}


class Hash18 {
  public int hash18(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[5] >>> 1) & 1 ) != 0 ));
bs.set(1, (((hash64[2] >>> 4) & 1 ) != 0 ));
bs.set(2, (((hash64[7] >>> 6) & 1 ) != 0 ));
bs.set(3, (((hash64[7] >>> 2) & 1 ) != 0 ));
bs.set(4, (((hash64[3] >>> 2) & 1 ) != 0 ));
bs.set(5, (((hash64[7] >>> 1) & 1 ) != 0 ));
bs.set(6, (((hash64[5] >>> 4) & 1 ) != 0 ));
bs.set(7, (((hash64[2] >>> 5) & 1 ) != 0 ));
bs.set(8, (((hash64[4] >>> 6) & 1 ) != 0 ));
bs.set(9, (((hash64[5] >>> 0) & 1 ) != 0 ));
bs.set(10, (((hash64[6] >>> 0) & 1 ) != 0 ));
bs.set(11, (((hash64[2] >>> 7) & 1 ) != 0 ));
bs.set(12, (((hash64[1] >>> 4) & 1 ) != 0 ));
bs.set(13, (((hash64[1] >>> 6) & 1 ) != 0 ));
bs.set(14, (((hash64[1] >>> 2) & 1 ) != 0 ));
bs.set(15, (((hash64[1] >>> 5) & 1 ) != 0 ));
bs.set(16, (((hash64[0] >>> 5) & 1 ) != 0 ));
bs.set(17, (((hash64[2] >>> 1) & 1 ) != 0 ));
bs.set(18, (((hash64[0] >>> 3) & 1 ) != 0 ));
bs.set(19, (((hash64[4] >>> 1) & 1 ) != 0 ));
 return bs.hashCode();}
}


class Hash19 {
  public int hash19(byte[] hash64) {
BitSet bs = new BitSet(20);

bs.set(0, (((hash64[4] >>> 4) & 1 ) != 0 ));
bs.set(1, (((hash64[1] >>> 0) & 1 ) != 0 ));
bs.set(2, (((hash64[5] >>> 1) & 1 ) != 0 ));
bs.set(3, (((hash64[0] >>> 6) & 1 ) != 0 ));
bs.set(4, (((hash64[6] >>> 5) & 1 ) != 0 ));
bs.set(5, (((hash64[0] >>> 5) & 1 ) != 0 ));
bs.set(6, (((hash64[7] >>> 2) & 1 ) != 0 ));
bs.set(7, (((hash64[6] >>> 2) & 1 ) != 0 ));
bs.set(8, (((hash64[0] >>> 0) & 1 ) != 0 ));
bs.set(9, (((hash64[3] >>> 0) & 1 ) != 0 ));
bs.set(10, (((hash64[4] >>> 1) & 1 ) != 0 ));
bs.set(11, (((hash64[4] >>> 3) & 1 ) != 0 ));
bs.set(12, (((hash64[1] >>> 6) & 1 ) != 0 ));
bs.set(13, (((hash64[3] >>> 2) & 1 ) != 0 ));
bs.set(14, (((hash64[5] >>> 6) & 1 ) != 0 ));
bs.set(15, (((hash64[4] >>> 6) & 1 ) != 0 ));
bs.set(16, (((hash64[1] >>> 2) & 1 ) != 0 ));
bs.set(17, (((hash64[4] >>> 0) & 1 ) != 0 ));
bs.set(18, (((hash64[3] >>> 4) & 1 ) != 0 ));
bs.set(19, (((hash64[4] >>> 5) & 1 ) != 0 ));
 return bs.hashCode();}
}


Hash0 hash0 = new Hash0();
Hash1 hash1 = new Hash1();
Hash2 hash2 = new Hash2();
Hash3 hash3 = new Hash3();
Hash4 hash4 = new Hash4();
Hash5 hash5 = new Hash5();
Hash6 hash6 = new Hash6();
Hash7 hash7 = new Hash7();
Hash8 hash8 = new Hash8();
Hash9 hash9 = new Hash9();
Hash10 hash10 = new Hash10();
Hash11 hash11 = new Hash11();
Hash12 hash12 = new Hash12();
Hash13 hash13 = new Hash13();
Hash14 hash14 = new Hash14();
Hash15 hash15 = new Hash15();
Hash16 hash16 = new Hash16();
Hash17 hash17 = new Hash17();
Hash18 hash18 = new Hash18();
Hash19 hash19 = new Hash19();



public final LSH[] hashers = new LSH[] {
new LSH() {@Override public int hash(byte[] hash64) { return hash0.hash0(hash64); } },
new LSH() {@Override public int hash(byte[] hash64) { return hash1.hash1(hash64); } },
new LSH() {@Override public int hash(byte[] hash64) { return hash2.hash2(hash64); } },
new LSH() {@Override public int hash(byte[] hash64) { return hash3.hash3(hash64); } },
new LSH() {@Override public int hash(byte[] hash64) { return hash4.hash4(hash64); } },
new LSH() {@Override public int hash(byte[] hash64) { return hash5.hash5(hash64); } },
new LSH() {@Override public int hash(byte[] hash64) { return hash6.hash6(hash64); } },
new LSH() {@Override public int hash(byte[] hash64) { return hash7.hash7(hash64); } },
new LSH() {@Override public int hash(byte[] hash64) { return hash8.hash8(hash64); } },
new LSH() {@Override public int hash(byte[] hash64) { return hash9.hash9(hash64); } },
new LSH() {@Override public int hash(byte[] hash64) { return hash10.hash10(hash64); } },
new LSH() {@Override public int hash(byte[] hash64) { return hash11.hash11(hash64); } },
new LSH() {@Override public int hash(byte[] hash64) { return hash12.hash12(hash64); } },
new LSH() {@Override public int hash(byte[] hash64) { return hash13.hash13(hash64); } },
new LSH() {@Override public int hash(byte[] hash64) { return hash14.hash14(hash64); } },
new LSH() {@Override public int hash(byte[] hash64) { return hash15.hash15(hash64); } },
new LSH() {@Override public int hash(byte[] hash64) { return hash16.hash16(hash64); } },
new LSH() {@Override public int hash(byte[] hash64) { return hash17.hash17(hash64); } },
new LSH() {@Override public int hash(byte[] hash64) { return hash18.hash18(hash64); } },
new LSH() {@Override public int hash(byte[] hash64) { return hash19.hash19(hash64); } }
};
