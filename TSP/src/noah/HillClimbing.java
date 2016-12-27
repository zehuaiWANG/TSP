package noah;  
  
import java.io.BufferedReader;  
import java.io.FileInputStream;  
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.util.Random;  
  
public class HillClimbing {  
  
    private int MAX_GEN;// ��������  
    private int cityNum; // ��������,���볤��  
    private int[][] distance; // �������  
    private int bestT;// ��ѳ��ִ���  
    private int[] bestGh;// ��õ�·������  
    private int bestEvaluation;  
  
    private Random random;  
  
    public HillClimbing() {  
  
    }  
  
    /** 
     * constructor of GA 
     *  
     * @param n 
     *            �������� 
     * @param g 
     *            ���д��� 
     *  
     **/  
    public HillClimbing(int n, int g) {  
        cityNum = n;  
        MAX_GEN = g;  
    }  
  
    // ��������һ��ָ��������Ա���ע�Ĵ���Ԫ���ڲ���ĳЩ���汣�־�Ĭ  
    @SuppressWarnings("resource")  
    /** 
     * ��ʼ��HillClimbing�㷨�� 
     * @param filename �����ļ��������ļ��洢���г��нڵ��������� 
     * @throws IOException 
     */  
    private void init(String filename) throws IOException {  
        // ��ȡ����  
        int[] x;  
        int[] y;  
        String strbuff;  
        BufferedReader data = new BufferedReader(new InputStreamReader(  
                new FileInputStream(filename)));  
        distance = new int[cityNum][cityNum];  
        x = new int[cityNum];  
        y = new int[cityNum];  
        for (int i = 0; i < cityNum; i++) {  
            // ��ȡһ�����ݣ����ݸ�ʽ1 6734 1453  
            strbuff = data.readLine();  
            // �ַ��ָ�  
            String[] strcol = strbuff.split(" ");  
            x[i] = Integer.valueOf(strcol[1]);// x����  
            y[i] = Integer.valueOf(strcol[2]);// y����  
        }  
        // ����������  
        // ��Ծ������⣬������㷽��Ҳ��һ����  
        // �˴��õ���att48��Ϊ����������48�����У�������㷽��Ϊαŷ�Ͼ��룬����ֵΪ10628  
        for (int i = 0; i < cityNum - 1; i++) {  
            distance[i][i] = 0; // �Խ���Ϊ0  
            for (int j = i + 1; j < cityNum; j++) {  
                double rij = Math  
                        .sqrt(((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j])  
                                * (y[i] - y[j])) / 10.0);  
                // �������룬ȡ��  
                int tij = (int) Math.round(rij);  
                if (tij < rij) {  
                    distance[i][j] = tij + 1;  
                    distance[j][i] = distance[i][j];  
                } else {  
                    distance[i][j] = tij;  
                    distance[j][i] = distance[i][j];  
                }  
            }  
        }  
        distance[cityNum - 1][cityNum - 1] = 0;  
  
        bestGh = new int[cityNum];  
        bestEvaluation = Integer.MAX_VALUE;  
        bestT = 0;  
  
        random = new Random(System.currentTimeMillis());  
    }  
  
    // ��ʼ������Ghh  
    void initGroup() {  
        int i, j;  
        bestGh[0] = random.nextInt(65535) % cityNum;  
        for (i = 1; i < cityNum;)// ���볤��  
        {  
            bestGh[i] = random.nextInt(65535) % cityNum;  
            for (j = 0; j < i; j++) {  
                if (bestGh[i] == bestGh[j]) {  
                    break;  
                }  
            }  
            if (j == i) {  
                i++;  
            }  
        }  
    }  
  
    public int evaluate(int[] chr) {  
        int len = 0;  
        // Ⱦɫ�壬��ʼ����,����1,����2...����n  
        for (int i = 1; i < cityNum; i++) {  
            len += distance[chr[i - 1]][chr[i]];  
        }  
        // ����n,��ʼ����  
        len += distance[chr[cityNum - 1]][chr[0]];  
        return len;  
    }  
  
    // ��ɽ�㷨  
    public void pashan(int[] Gh, int T) {  
        int i, temp, tt = 0;  
        int ran1, ran2;  
        int e;// ������ֵ  
        int[] tempGh = new int[cityNum];  
        bestEvaluation = evaluate(Gh);  
  
        // ��ɽ����T  
        for (tt = 0; tt < T; tt++) {  
            for (i = 0; i < cityNum; i++) {  
                tempGh[i] = Gh[i];  
            }  
            ran1 = random.nextInt(65535) % cityNum;  
            ran2 = random.nextInt(65535) % cityNum;  
            while (ran1 == ran2) {  
                ran2 = random.nextInt(65535) % cityNum;  
            }  
  
            // ��������ʵʩ�������  
            temp = tempGh[ran1];  
            tempGh[ran1] = tempGh[ran2];  
            tempGh[ran2] = temp;  
  
            e = evaluate(tempGh);// ������ֵ  
  
            if (e < bestEvaluation) {  
                bestT = tt;  
                bestEvaluation = e;  
                for (i = 0; i < cityNum; i++) {  
                    Gh[i] = tempGh[i];  
                }  
            }  
        }  
  
    }  
  
    public void solve() {  
        initGroup();// ��ʼ������  
        pashan(bestGh, MAX_GEN);  
  
        System.out.println("��ѳ��ȳ��ִ�����");  
        System.out.println(bestT);  
        System.out.println("��ѳ���");  
        System.out.println(bestEvaluation);  
        System.out.println("���·����");  
        for (int i = 0; i < cityNum; i++) {  
            System.out.print(bestGh[i] + ",");  
            if (i % 10 == 0 && i != 0) {  
                System.out.println();  
            }  
        }  
    }  
  
    /** 
     * @param args 
     * @throws IOException 
     */  
    public static void main(String[] args) throws IOException {  
        System.out.println("Start....");  
        HillClimbing hillClimbing = new HillClimbing(48, 5000);  
        hillClimbing.init("c://data.txt");  
        hillClimbing.solve();  
    }  
}  
