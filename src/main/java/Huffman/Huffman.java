package Huffman;

import Modelhandler.Vector;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;
import myFile.corpusReader;

import java.io.*;
import java.util.*;

public class Huffman {
//叶子节点的名字和其huffman编码
    public HashMap<String, byte[]> tree=new HashMap<String, byte[]>();
//    最优二叉树
    public node Tree=new node();
//    叶子节点的名字set
    public Set<String> Termset;
//    非叶子节点的huffman编码与其向量的hashmap
    public HashMap<String,Vector> huff_vector=new HashMap<String, Vector>();
//    huffman编码的最大长度
    private static int maxlenthofhuffman=0;
//    非叶子节点的名字和其huffman编码
    public HashMap<String, byte[]> treenotleaf=new HashMap<String, byte[]>();

    public HashMap<String,Integer> dictionary=new HashMap<String, Integer>();

    public int totalnum=0;

    public List<String> lowwords=new ArrayList<String>();
    String[] stopwords_str={"an","'d","'ll","'m","'re","'s","'t","'ve","ZT","ZZ","a","a's","able","about","above","abst","accordance","according","accordingly","across",
            "act","actually","added","adj","adopted","affected","affecting","affects","after","afterwards",
            "again","against","ah","ain't","all","allow","allows","almost","alone","along","already","also",
            "although","always","am","among","amongst","an","and","announce","another","any","anybody","anyhow",
            "anymore","anyone","anything","anyway","anyways","anywhere","apart","apparently","appear","appreciate",
            "appropriate","approximately","are","area","areas","aren","aren't","arent","arise","around","as","aside",
            "ask","asked","asking","asks","associated","at","auth","available","away","awfully","b","back","backed","backing",
            "backs","be","became","because","become","becomes","becoming","been","before","beforehand","began","begin","beginning",
            "beginnings","begins","behind","being","beings","believe","below","beside","besides","best","better","between","beyond",
            "big","biol","both","brief","briefly","but","by","c","c'mon","c's","ca","came","can","can't","cannot","cant","case","cases",
            "cause","causes","certain","certainly","changes","clear","clearly","co","com","come","comes","concerning","consequently","consider",
            "considering","contain","containing","contains","corresponding","could","couldn't","couldnt","course","currently","d","date","definitely",
            "describe","described","despite","did","didn't","differ","different","differently","discuss","do","does","doesn't","doing","don't","done",
            "down","downed","downing","downs","downwards","due","during","e","each","early","ed","edu","effect","eg","eight","eighty","either","else",
            "elsewhere","end","ended","ending","ends","enough","entirely","especially","et","et-al","etc","even","evenly","ever","every","everybody",
            "everyone","everything","everywhere","ex","exactly","example","except","f","face","faces","fact","facts","far","felt","few","ff","fifth",
            "find","finds","first","five","fix","followed","following","follows","for","former","formerly","forth","found","four","from","full","fully",
            "further","furthered","furthering","furthermore","furthers","g","gave","general","generally","get","gets","getting","give","given","gives",
            "giving","go","goes","going","gone","good","goods","got","gotten","great","greater","greatest","greetings","group","grouped","grouping",
            "groups","h","had","hadn't","happens","hardly","has","hasn't","have","haven't","having","he","he's","hed","hello","help","hence","her",
            "here","here's","hereafter","hereby","herein","heres","hereupon","hers","herself","hes","hi","hid","high","higher","highest","him","himself",
            "his","hither","home","hopefully","how","howbeit","however","hundred","i","i'd","i'll","i'm","i've","id","ie","if","ignored","im","immediate",
            "immediately","importance","important","in","inasmuch","inc","include","indeed","index","indicate","indicated","indicates","information","inner",
            "insofar","instead","interest","interested","interesting","interests","into","invention","inward","is","isn't","it","it'd","it'll","it's",
            "itd","its","itself","j","just","k","keep","keeps","kept","keys","kg","kind","km","knew","know","known","knows","l","large","largely",
            "last","lately","later","latest","latter","latterly","least","less","lest","let","let's","lets","like","liked","likely","line","little",
            "long","longer","longest","look","looking","looks","ltd","m","made","mainly","make","makes","making","man","many","may","maybe","me",
            "mean","means","meantime","meanwhile","member","members","men","merely","mg","might","million","miss","ml","more","moreover","most",
            "mostly","mr","mrs","much","mug","must","my","myself","n","n't","na","name","namely","nay","nd","near","nearly","necessarily",
            "necessary","need","needed","needing","needs","neither","never","nevertheless","new","newer","newest","next","nine","ninety",
            "no","nobody","non","none","nonetheless","noone","nor","normally","nos","not","noted","nothing","novel","now","nowhere",
            "number","numbers","o","obtain","obtained","obviously","of","off","often","oh","ok","okay","old","older","oldest","omitted",
            "on","once","ones","only","onto","open","opened","opening","opens","or","ord","order","ordered","ordering","orders",
            "other","others","otherwise","ought","our","ours","ourselves","out","outside","over","overall","owing","own","p","page","pages",
            "part","parted","particular","particularly","parting","parts","past","per","perhaps","place","placed","places","please","plus",
            "point","pointed","pointing","points","poorly","possible","possibly","potentially","pp","predominantly","present","presented",
            "presenting","presents","presumably","previously","primarily","probably","problem","problems","promptly","proud","provides","put",
            "puts","q","que","quickly","quite","qv","r","ran","rather","rd","re","readily","really","reasonably","recent","recently","ref",
            "refs","regarding","regardless","regards","related","relatively","research","respectively","resulted","resulting","results",
            "right","room","rooms","run","s","said","same","saw","say","saying","says","sec","second","secondly","seconds","section",
            "see","seeing","seem","seemed","seeming","seems","seen","sees","self","selves","sensible","sent","serious","seriously",
            "seven","several","shall","she","she'll","shed","shes","should","shouldn't","show","showed","showing","shown","showns",
            "shows","side","sides","significant","significantly","similar","similarly","since","six","slightly","small","smaller",
            "smallest","so","some","somebody","somehow","someone","somethan","something","sometime","sometimes","somewhat",
            "somewhere","soon","sorry","specifically","specified","specify","specifying","state","states","still","stop",
            "strongly","sub","substantially","successfully","such","sufficiently","suggest","sup","sure","t","t's","take",
            "taken","taking","tell","tends","th","than","thank","thanks","thanx","that","that'll","that's","that've",
            "thats","the","their","theirs","them","themselves","then","thence","there","there'll","there's","there've",
            "thereafter","thereby","thered","therefore","therein","thereof","therere","theres","thereto","thereupon",
            "these","they","they'd","they'll","they're","they've","theyd","theyre","thing","things","think","thinks",
            "third","this","thorough","thoroughly","those","thou","though","thoughh","thought","thoughts","thousand",
            "three","throug","through","throughout","thru","thus","til","tip","to","today","together","too","took",
            "toward","towards","tried","tries","truly","try","trying","ts","turn","turned","turning","turns","twice",
            "u","un","under","unfortunately","unless","unlike","unlikely","until","unto","up","upon","ups",
            "us","use","used","useful","usefully","usefulness","uses","using","usually","uucp","v","value","various",
            "very","via","viz","vol","vols","vs","w","want","wanted","wanting","wants","was","wasn't","way","ways",
            "we","we'd","we'll","we're","we've","wed","welcome","well","wells","went","were","weren't","what","what'll",
            "what's","whatever","whats","when","whence","whenever","where","where's","whereafter","whereas","whereby",
            "wherein","wheres","whereupon","wherever","whether","which","while","whim","whither","who","who'll","who's",
            "whod","whoever","whole","whom","whomever","whos","whose","why","widely","will","willing","wish","with",
            "within","without","won't","wonder","words","work","worked","working","works","world","would","wouldn't",
            "www","x","y","year","years","yes","yet","you","you'd","you'll","you're","you've","youd","young","younger",
            "youngest","your","youre","yours","yourself","yourselves","z","zero","zt","zz"};
    List<String> stopwords=new ArrayList<String>();
    String language;
    JiebaSegmenter segmenter = new JiebaSegmenter();
    public int getMaxlenthofhuffman() {
        return maxlenthofhuffman;
    }

    public Vector getVectorofnotleafbyHuffman(String huffman){


        return huff_vector.get(huffman);
    }

    public Vector getNodevector(byte[] path){
        node n=Tree;
        for(int i=0;i<path.length;i++){
            if(path[i]==1){
                n=n.getLeftnode();
            }
            if(path[i]==0){
                n=n.getRightnode();
            }
        }
        return n.getVec();
    }





    public boolean equals(byte[] a,byte[] b){
        if(a.length!=b.length){
            return false;
        }else{
            for(int i=0;i<a.length;i++){
                if (a[i]!=b[i]){
                    return false;
                }
            }
        }
        return true;
    }
    public void setVectorofnotleafbyHuffman(String huffman,Vector vec){
        huff_vector.put(huffman,vec);
    }


    private void gennaratehuff_vector(int size){
        for(String e:treenotleaf.keySet()){
            huff_vector.put(bytetoString(treenotleaf.get(e)),new Vector(size,0));
        }
        huff_vector.put("-1",new Vector(size,0));
    }

    private String bytetoString(byte[] in){
        String out="";
        for(byte e:in){
            out+=Byte.toString(e);
        }
        return out;
    }

    public Huffman(){
    }

    public byte[] next_binary(byte[] in){
        int size=in.length;
        int indx=size-1;
        int up=1;
        while(up==1&&indx!=-1){
            int tmp=in[indx]+up;
            if(tmp>1){
                in[indx]=(byte)(tmp%2);
                indx-=1;
            }else{
                in[indx]=(byte)tmp;
                up=0;
            }
        }
        if(up==1){
            byte[] newin=new byte[size+1];
            newin[0]=1;
            for(int i=0;i<size;i++){
                newin[i+1]=in[i];
            }
            return newin;
        }
        return in;
    }

//  根据编码获取原信息
    public String encode(byte[] huffman){
        node dicnode=Tree;
        for(byte e:huffman){

            if(e==(byte)0){
                dicnode=dicnode.getLeftnode();
            }else{
                dicnode=dicnode.getRightnode();
            }
        }
        return dicnode.getName();
    }

    public void setnodevec(byte[] huffman,Vector in){
        node nd=new node();
        for(byte e:huffman){
            if(e==0){
                nd=Tree.getLeftnode();
            }else{
                nd=Tree.getRightnode();
            }
        }
    }

//  两个构造方法，可以分别通过地址读取语料库 或者直接使用hashmap语料库
    public Huffman(HashMap<String, Integer> corpus,int size) {
        generatetree(corpus,size);
    }

    public Huffman(String corpuspath,int size,String language) {

        this.language=language;
        for(String e:stopwords_str){
            stopwords.add(e);
        }
        readcorpus(corpuspath,language);
        generatetree(dictionary,size);
    }

//  获取单个元素的huffman编码
    public byte[] getHuffmancode(String name){
        return tree.get(name);
    }

    public String getHuffmancode_str(String name){
        String out="";
        for(byte e:tree.get(name)){
            out+=Byte.toString(e);
        }
        return out;
    }

//    叶子节点的输出
    public void leafstoString(){
        for(String e:tree.keySet()){
            String out="";
            out+=e+"=";
            for (byte b:tree.get(e)){
                out+=Byte.toString(b);
            }
            System.out.println(out);
        }
    }

    public void notleafstoString(){
        for(String e:treenotleaf.keySet()){
            String out="";
            out+=e+"=";
            for (byte b:treenotleaf.get(e)){
                out+=Byte.toString(b);
            }
            System.out.println(out);
        }
    }


//  生成huffman编码对应表
    private void generatetree(HashMap<String, Integer> corpus,int size){
        Termset=corpus.keySet();
        List<Map.Entry<String, Integer>> infoIds =
                new ArrayList<Map.Entry<String, Integer>>(corpus.entrySet());
        Collections.sort(infoIds, new Comparator<HashMap.Entry<String, Integer>>() {
            public int compare(HashMap.Entry<String, Integer> o1, HashMap.Entry<String, Integer> o2) {
                return (o1.getValue() - o2.getValue());
            }
        });
        List<node> nodeset=toNodeset(infoIds);
        List<node> newnodeset=new ArrayList<node>();
        int idofunleaf=0;
        node newnode=new node();
        Set<String> nameofnotleaf=new HashSet<String>();
        while(nodeset.size()!=1) {
//            选择nodeset中最小的两个
            newnode=addnode(nodeset.get(0),nodeset.get(1),idofunleaf,size);
            nameofnotleaf.add(newnode.getName());
            idofunleaf+=1;
//            将新结点加入newset
            newnodeset.add(newnode);
//            更新nodeset
            nodeset.remove(0);
            nodeset.remove(0);
            nodeset.add(0,newnode);
            for(int i=0;i<nodeset.size();i++){
                if(nodeset.get(i).isIsleaf()){
                    break;
                }
            }
            nodeset = nodesort(nodeset);
        }
        Tree=newnode;
        listcode( newnodeset, corpus.keySet(),nameofnotleaf);
        gennaratehuff_vector(size);
        System.out.println("deep of Huffman tree is "+Integer.toString(maxlenthofhuffman)+"");
    }

//  对元素集合编码
    private void listcode(List<node> nodeset,Set<String> nameset,Set<String> namesetnotleaf){
        HashMap<String,String> leftList=new HashMap<String,String>();
        HashMap<String,String> rightList=new HashMap<String,String>();
        for (node e:nodeset){
            leftList.put(e.getLeftnode().getName(),e.getName());
            rightList.put(e.getRightnode().getName(),e.getName());
        }
        for (String e:nameset){
            tree.put(e,tobyte(code(leftList,rightList,e)));
        }
        for(String e:namesetnotleaf){
            treenotleaf.put(e,tobyte(code(leftList,rightList,e)));
        }
//        System.out.println();
    }

//  String转byte[]
    private byte[] tobyte(String code){
        byte[] out=new byte[code.length()];
        for (int i=0;i<code.length();i++){
            out[i]=Byte.parseByte(code.substring(i,i+1));
        }
        if(out.length>maxlenthofhuffman){
            maxlenthofhuffman=out.length;
        }
        return out;
    }

//    对单个元素进行编码
//    我又检查了一边这个函数应该是对的，但这个逻辑好牛逼，我好像现在想不到这个逻辑了
    private String code(HashMap<String,String> leftList,HashMap<String,String> rightList,String name){
        String out="";
        while(leftList.containsKey(name) || rightList.containsKey(name)){
            if(leftList.containsKey(name)){
                out="1"+out;
                name=leftList.get(name);
            }
            if(rightList.containsKey(name)){
                out="0"+out;
                name=rightList.get(name);
            }
        }
        return out;
    }

//  读取语料文件
    private void readcorpus(String corpuspath,String language) {
        try {
            File F = new File(corpuspath);
            File[] filelist = F.listFiles();
            if(language=="cn") {
                for (File f : filelist) {
                    if (!f.isDirectory()) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                        String tempbyte="";
                        String sentence="";
                        while ((tempbyte = br.readLine()) != null) {
                            sentence+=tempbyte;
                        }
                        br.close();
                        List<SegToken> words=segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);
                        for(SegToken e:words){
                            if (dictionary.containsKey(e.word)) {
                                dictionary.put(e.word, dictionary.get(e.word) + 1);
                            } else {
                                dictionary.put(e.word, 1);
                            }
                        }
                    }
                }
            }else{
                for (File f : filelist) {
                    if (!f.isDirectory()) {
                        Reader br = new InputStreamReader(new FileInputStream(f));
                        int tempbyte;
                        String term = "";
                        while ((tempbyte = br.read()) != -1) {
                            char w = (char) tempbyte;
                            if (Character.isSpaceChar(w) || tempbyte == 10 || tempbyte == 13) {
                                if (term != "" && !stopwords.contains(term)) {
                                    totalnum++;
                                    if (dictionary.containsKey(term)) {
                                        dictionary.put(term, dictionary.get(term) + 1);
                                    } else {
                                        dictionary.put(term, 1);
                                    }
                                }
                                term = "";
                            } else {
                                term += String.valueOf(w);
                            }
                        }
                        br.close();
                    }
                }
            }
            lowwords = new ArrayList<String>();
            for (String e : dictionary.keySet()) {
                if (dictionary.get(e) < 10) {
                    lowwords.add(e);
                }
            }
            for (String e : lowwords) {
                dictionary.remove(e);
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

//  将语料库转化为标准形式
    private List<node> toNodeset(List<Map.Entry<String, Integer>> infoIds){
        List<node> nodeset=new ArrayList<node>();
        for(Map.Entry<String, Integer> each:infoIds){
            node nd=new node();
            nd.setIsleaf(true);
            nd.setWeight(each.getValue());
            nd.setName(each.getKey());
            nodeset.add(nd);
        }
        return nodeset;
    }

//  对元素按权值排序
    private List<node> nodesort(List<node> nodeset){
        int weight=nodeset.get(0).getWeight();
        for (int i=0;i<nodeset.size();i++){
            if(nodeset.get(i).getWeight()>weight){
                nodeset.add(i,nodeset.get(0));
                nodeset.remove(0);
                break;
            }
        }
        return nodeset;
    }

//  元素的合并
    private node addnode(node nd1,node nd2,int id,int size){
        node nd=new node();
        nd.setName("N-"+Integer.toString(id));
        nd.setVec(new Vector(size));
        nd.setIsleaf(false);
        nd.setWeight(nd1.getWeight()+nd2.getWeight());
        nd.setLeftnode(nd1);
        nd.setRightnode(nd2);
        return nd;
    }

}
