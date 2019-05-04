import java.math.BigInteger;

public class parser {

	parser() {
		
	}
	
	public BigInteger parseBi(String s) {
		BigInteger bi = new BigInteger("0");
		double value = 0;
		if(s.length()>500) {return new BigInteger("-1");}
		for(int i=(s.length()-1);i>=0;i--) {
			String check = s.substring(s.length()-1-i,s.length()-i);
			if(check.equalsIgnoreCase("a")) {
				double addvalue=0*Math.pow(4, i);
				value+= addvalue;
				bi.add((((new BigInteger("4")).pow(i)).multiply((new BigInteger("0")))));
			}else if(check.equalsIgnoreCase("t")) {
				double addvalue=3*Math.pow(4, i);
				value+= addvalue;
				bi.add(new BigInteger("4").pow(i).multiply(new BigInteger("3")));
			}else if(check.equalsIgnoreCase("c")) {
				double addvalue=1*Math.pow(4, i);
				value+= addvalue;
				bi.add(new BigInteger("4").pow(i).multiply(new BigInteger("1")));
			}else if(check.equalsIgnoreCase("g")) {
				double addvalue=2*Math.pow(4, i);
				value+= addvalue;
				bi.add(new BigInteger("4").pow(i).multiply(new BigInteger("2")));
			}
			System.out.println(bi.toString());
		}
		return bi;
	}
	
	public long parselong(String s) {
		long value = 0;
		if(s.length()>31) {return (long)(-1);}
		for(int i=(s.length()-1);i>=0;i--) {
			String check = s.substring(s.length()-1-i,s.length()-i);
			if(check.equalsIgnoreCase("a")) {
				double addvalue=0*Math.pow(4, i);
				value+= addvalue;
			}else if(check.equalsIgnoreCase("t")) {
				double addvalue=3*Math.pow(4, i);
				value+= addvalue;
			}else if(check.equalsIgnoreCase("c")) {
				double addvalue=1*Math.pow(4, i);
				value+= addvalue;
			}else if(check.equalsIgnoreCase("g")) {
				double addvalue=2*Math.pow(4, i);
				value+= addvalue;
			}
			System.out.println(value);
		}
		return value;
	}
	
	public double parsedouble(String s) {
		double value = 0;
		if(s.length()>31) {return (long)(-1);}
		for(int i=(s.length()-1);i>=0;i--) {
			String check = s.substring(s.length()-1-i,s.length()-i);
			if(check.equalsIgnoreCase("a")) {
				double addvalue=0*Math.pow(4, i);
				value+= addvalue;
			}else if(check.equalsIgnoreCase("t")) {
				double addvalue=3*Math.pow(4, i);
				value+= addvalue;
			}else if(check.equalsIgnoreCase("c")) {
				double addvalue=1*Math.pow(4, i);
				value+= addvalue;
			}else if(check.equalsIgnoreCase("g")) {
				double addvalue=2*Math.pow(4, i);
				value+= addvalue;
			}
			System.out.println(value);
		}
		return value;
	}
	
	
	
	public String unparse(double d,int length) {
		double change = d%(Math.pow(4, length));
		String value="";
		for(int i=length-1;i>=0;i--) {
			int charvalue = (int)(Math.floor(change/(Math.pow(4, i))));
			change=change%(Math.pow(4, i));
			if(charvalue==0) {
				value = value+"A";
			}else if(charvalue==3) {
				value = value+"T";
			}else if(charvalue==1) {
				value = value+"C";
			}else if(charvalue==2) {
				value = value+"G";
			}
		}

		return value;
	}
		
		
		public String unparse(long d,int length) {
			long change = (long) (d%(Math.pow(4, length)));
			String value="";
			for(int i=length-1;i>=0;i--) {
				int charvalue = (int)(Math.floor(change/(Math.pow(4, i))));
				change=(long) (change%(Math.pow(4, i)));
				if(charvalue==0) {
					value = value+"A";
				}else if(charvalue==3) {
					value = value+"T";
				}else if(charvalue==1) {
					value = value+"C";
				}else if(charvalue==2) {
					value = value+"G";
				}
			}
		
		return value;
	}
	
	
}
