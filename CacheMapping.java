import java.security.spec.RSAOtherPrimeInfo;
import java.util.*;
import java.io.*;




/** Class for buffered reading int and double values */
class Reader {
    static BufferedReader reader;
    static StringTokenizer tokenizer;

    /** call this method to initialize reader for InputStream */
    static void init(InputStream input) {
        reader = new BufferedReader(
                new InputStreamReader(input) );
        tokenizer = new StringTokenizer("");
    }

    /** get next word */
    static String next() throws IOException {
        while ( ! tokenizer.hasMoreTokens() ) {
            //TODO add check for eof if necessary
            tokenizer = new StringTokenizer(
                    reader.readLine() );
        }
        return tokenizer.nextToken();
    }

    static int nextInt() throws IOException {
        return Integer.parseInt( next() );
    }

    static double nextDouble() throws IOException {
        return Double.parseDouble( next() );
    }
}



class Main {
    public static int N, K, C, R, r, n, k, c;
    public static String[] taggers;
    public static String[][] cache;
    public static int type = 0;
    public static String data;
    public static Queue<String> q= new LinkedList<>();             // for applying FIFO replacement policy

    public static int stringToDecimal(String s)
    {
        int result = 0;
        for(int i = s.length()-1;i>=0;i--)
        {
            if(s.charAt(i)=='1')
            {
                result += Math.pow(2,Math.abs((s.length()-1)-i));
            }
            else
            {
                result+=0;
            }
        }
        return result;
    }

    public static int log2(int x)
    {
        return (int)(Math.log(x)/Math.log(2));

    }


    public static void directMapped(String phy_add) {

        if(type ==3)
        {
            System.out.println("\t\t"+"Cache Memory");
            System.out.println("__________________________________");
            for (int i = 0; i < C; i++)
            {
                System.out.print("Line "+i+": ");
                for (int j = 0; j < K; j++)
                {
                    System.out.print(cache[i][j] + " ");
                }
                System.out.print("\ttag :" + taggers[i]);

                System.out.println();
            }
            System.out.println("__________________________________");

            return;
        }

        String block_offset = phy_add.substring(phy_add.length() - k); // block_offset is equal to the k

        String cache_line = phy_add.substring(phy_add.length() - k - c, phy_add.length() - k); // cache_line is equal to c
        String tag = phy_add.substring(0, phy_add.length() - k - c);  // tag is equal to the n-(k+c)
        String ncacheLine = tag + cache_line;
        int newCl = stringToDecimal(ncacheLine);
        int cacheLine = stringToDecimal(cache_line);
        int blockOff = stringToDecimal(block_offset);


        if(type==1)
        {
            if(cache[cacheLine][blockOff]==null)
            {
                System.out.println("Address not found!");
            }
            else
            {

                System.out.println("Cache Hit!");
                System.out.println("data : "+cache[cacheLine][blockOff]);

            }
        }
        else if(type==2)
        {
            cacheLine = newCl % C;
            cache[cacheLine][blockOff] = data;
            if(taggers[cacheLine]==null)
            {
                taggers[cacheLine] = tag;
            }
            else
            {
                String x = taggers[cacheLine];
                System.out.println(x + " this is x");
                taggers[cacheLine] = tag;
                System.out.println("Tag "+x + " has been replaced by " + tag);
            }
        }



    }


    public static void fullyAssociative(String phy_add)
    {

         if(type ==3)
        {
            System.out.println("\t\t"+"Cache Memory");
            System.out.println("__________________________________");
        for (int i = 0; i < C; i++)
        {

            System.out.print("Line "+i+": ");
            for (int j = 0; j < K; j++)
            {
                System.out.print(cache[i][j] + " ");
            }
            System.out.print("\ttag :" + taggers[i]);
            System.out.println();
        }
            System.out.println("__________________________________");
        return;
    }

        String block_offset = phy_add.substring(phy_add.length() - k); // block_offset is equal to the k

        String tag = phy_add.substring(0, phy_add.length() - k);  // tag is equal to the n-(k+c)
        int blockOff = stringToDecimal(block_offset);

        if(type==1)
        {
            for(int i = 0;i<taggers.length-1;i++) // indicates the no. of rows
            {
                if(taggers[i].equals(tag))
                {
                    System.out.println("Cache Hit!");
                    System.out.println(cache[i][blockOff]);
                }
                else
                {
                    System.out.println("Address not found!");
                }
            }
        }
        else if(type==2)
        {
            int counter =0;
            for(String t:taggers)
            {
                if(t != null)
                {
                    counter++;
                }
                else
                {
                    break;
                }
            }
            if(counter==C)
            {
                String removed = q.poll();
                for(int i =0;i<taggers.length;i++)
                {
                    assert removed != null;
                    if(removed.equals(taggers[i]))
                    {
                        taggers[i] = tag;
                        cache[i][blockOff]  = data;
                        q.add(tag);
                        System.out.println("Tag "+removed + " has been replaced by "+ tag);
                    }
                }
            }
            else if(counter<C)
            {
                for (int i = 0; i < taggers.length; i++) {
                    if(taggers[i]==null)
                    {
                        cache[i][blockOff] = data;
                        taggers[i] = tag;
                        q.add(tag);
                        break;
                    }
                    else if (taggers[i].equals(tag))
                    {
                        cache[i][blockOff] = data;
                        break;
                    }


                }
            }


        }



    }

    public static void setAssociative(String phy_add)
    {

         if(type ==3) {
             System.out.println("\t\t"+"Cache Memory");
             System.out.println("__________________________________");
        for (int i = 0; i < C; i++)
        {
            System.out.print("Line "+i+": ");
            for (int j = 0; j < K; j++)
            {
                System.out.print(cache[i][j] + " ");
            }
            System.out.print("\ttag :" + taggers[i]);

            System.out.println();
            if(i%R!=0)
            {
                System.out.println("__________________________________");
            }
        }
             System.out.println("__________________________________");


             return;
    }

        String block_offset = phy_add.substring(phy_add.length() - k); // block_offset is equal to the k
        String set_no = phy_add.substring(phy_add.length() - k-r, phy_add.length()-k);
        String tag = phy_add.substring(0, phy_add.length()-k-r);
        String newSet = tag+set_no;
        int x = stringToDecimal(newSet);
        int blockOff = stringToDecimal(block_offset);
        int sn = x%R;
        int counter = 0;
        boolean check = false;
        Arrays.fill(taggers, "null");
        int index=-1;
        if(type==1)
        {
            for(int i = sn*R;i<=((sn*R)+(R-1));i++)
            {
                if(taggers[i].equals(tag))
                {
                    System.out.println("Cache Hit!");
                    System.out.println("data :" + cache[i][blockOff]);
                }
            }
        }

        else if(type==2)
        {
            for(int i =sn*R;i<=(sn*R)+(R-1);i++)
            {
                if(!taggers[i].equals("null"))
                {
                    counter++;
                }
            }

            if(counter<(sn*R)+(R-1))
            {
                for(int i =sn*R;i<=((sn*R)+(R-1));i++)
                {
                    if(taggers[i].equals(tag))
                    {
                        index = i;
                        check = true;
                        break;
                    }
                }
                if(check)
                {
                    taggers[index] = tag;
                    cache[index][blockOff] = data;
                }
                else
                {
                    for(int i = sn*R;i<=((sn*R)+(R-1));i++)
                    {
                        if(taggers[i].equals("null"))
                        {
                            taggers[i] = tag;
                            cache[i][blockOff] = data;
                        }
                    }
                }
            }

            else if(counter==(sn*R)+(R-1))
            {
                String removed = taggers[sn*R];
                taggers[sn*R] = tag;
                cache[sn*R][blockOff] = data;
                System.out.println(removed + " has been replaced by "+tag);
            }
        }

    }


    public static void main(String[] args) throws IOException
    {
        Reader.init(System.in);
        System.out.println("Enter the total number of lines in cache :");
         C = Reader.nextInt();
         if(C%2!=0)
         {
             System.out.println("Invalid Input!");
             return;
         }
        System.out.println("Enter the block size :");
         K = Reader.nextInt();
        if(K%2!=0)
        {
            System.out.println("Invalid Input!");
            return;
        }
        System.out.println("Enter the memory size"); // total addresses/words in main memory
         N = Reader.nextInt();
        if(N%2!=0)
        {
            System.out.println("Invalid Input!");
            return;
        }
        boolean checkSet = false;

        taggers = new String[C];
        cache = new String[C][K];
        String phy_add = "";


        // to calculate the no. of bits to represent the respective sizes
        n = log2(N);
        k= log2(K);
        c = log2(C);

        System.out.println("Mapping Required :");
        System.out.println("(D)DirectMapping " + "/(F)FullyAssociative " + "/(S)R-waySetAssociative");

        String map = Reader.next();
        if(map.equals("S"))
        {
            System.out.println("Enter the value of R");
            R = Reader.nextInt();
            r = log2(R);
        }
        while(true)
        {


            System.out.println("(r)Read/(w)Write/(d)DisplayCache");
            String mode = Reader.next();


            // this is to check if we have to read or write or display the entire cache

            if (mode.equals("r"))
            {
                type = 1;
                System.out.println("Enter the physical address");
                 phy_add = Reader.next();
            }
            else if (mode.equals("w"))
            {
                System.out.println("Enter the physical address");

                 phy_add = Reader.next();
                 if(phy_add.length()!=n)
                 {
                     System.out.println("Wrong Physical Address Given!");
                     break;
                 }
                System.out.println("Enter the data to be inserted");
                type = 2;
                data = Reader.next();
            }
            else if (mode.equals("d"))
            {
                type = 3;
            }


            if (map.equals("D"))
            {
                // direct mapping is initialized
                directMapped(phy_add);

            }
            else if(map.equals("F"))
            {
                fullyAssociative(phy_add);
            }
            else if(map.equals("S"))
            {

                setAssociative(phy_add);
            }

        }
    }
}