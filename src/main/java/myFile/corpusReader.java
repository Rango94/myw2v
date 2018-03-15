package myFile;

import Modelhandler.Model;
import Modelhandler.Vector;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;

import java.io.*;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

public class corpusReader {

    int Windos;
    boolean weatherfill;
    String Path;
    public List<String>  stopwords=new ArrayList<String>();
    int sperate=2000;
    static HashMap<Integer,Reader> filebr =new HashMap<Integer, Reader>();
    static HashMap<Integer,BufferedReader> filebr_cn =new HashMap<Integer, BufferedReader>();
    File[] filelist;
    JiebaSegmenter segmenter = new JiebaSegmenter();

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
    public corpusReader(String path,int windos,boolean weatherfill, List<String> lowwords,String language){

        File F=new File(path);
        try {
            filelist=F.listFiles();
            int num=0;
            if(filelist!=null) {
                if(language=="cn") {
                    for (File e : filelist) {
                        if (!e.isDirectory()) {
                            filebr_cn.put(num, new BufferedReader(new InputStreamReader(new FileInputStream(e), "utf-8")));
                            num++;
                        }
                    }
                }else{
                    for (File e : filelist) {
                        if (!e.isDirectory()) {
                            filebr.put(num, new InputStreamReader(new FileInputStream(e), "utf-8"));
                            num++;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.Path=path;
        this.weatherfill=weatherfill;
        this.Windos=windos;
        for(String e:stopwords_str){
            stopwords.add(e);
        }
        for(String e:lowwords){
            stopwords.add(e);
        }
    }

    public corpusReader(){
        for(String e:stopwords_str){
            stopwords.add(e);
        }
    }


    public HashMap<List<String>,Vector> handlesent_cbow(Model mod,int windos){
        HashMap<List<String>,Vector> subcorpus=new HashMap<List<String>,Vector>();
        try {
            int tempchar;
            String term="";
            int num=0;
            String[] line=new String[sperate];
            int idx=(int)(Math.random()* filebr.size());
            Reader br= filebr.get(idx);
            tempchar=br.read();
            while(tempchar!=-1){
                char w=(char)tempchar;
                if(tempchar==10||tempchar==13||Character.isSpaceChar(w)){
                    if(term!="" && !stopwords.contains(term)){
                        line[num]=term;
                        num+=1;
                    }
                    term="";
                }
                else{
                    term+=String.valueOf(w);
                }
                if(num==sperate){
                    break;
                }
                tempchar=br.read();
            }
            if(tempchar==-1){
                filebr.put(idx,new InputStreamReader (new FileInputStream(filelist[idx])));
            }
            if(num<sperate-1){
                for(int i=num;i<sperate;i++){
                    line[i]=line[num-1];
                }
            }
            for (int i = windos; i < line.length - windos; i++) {
                Vector input = new Vector(mod.getSize(),0);
                for (int j = i - windos; j < i + windos; j++) {
                    if (j == i) {
                        continue;
                    }
                    Vector tmp=mod.getVector(line[j]);
                    if (tmp!=null) {
                        input = Vector.adds(input,tmp);
                    }else{
                        System.out.println("发现单词"+line[j]+"没有对应向量");
                    }
                }
                subcorpus.put(substring(line, i - windos, i + windos+1), input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        if(Math.random()>0.8){
//            System.out.println(subcorpus.size());
//        }
        return subcorpus;
    }


    public HashMap<List<String>,Vector> handlesent_sg(Model mod,int windos){
        HashMap<List<String>,Vector> subcorpus=new HashMap<List<String>,Vector>();
        try {
            int tempchar;
            String term="";
            int num=0;
            String[] line=new String[sperate];
            int idx=(int)(Math.random()* filebr.size());
            System.out.println(filebr.size());
            System.out.println(idx);
            Reader br= filebr.get(idx);
            tempchar=br.read();
            while(tempchar!=-1){
                char w=(char)tempchar;
                if(tempchar==10||tempchar==13||Character.isSpaceChar(w)){
                    if(term!="" && !stopwords.contains(term)){
                        line[num]=term;
                        num+=1;
                    }
                    term="";
                }
                else{
                    term+=String.valueOf(w);
                }
                if(num==sperate){
                    break;
                }
                tempchar=br.read();
            }
            if(tempchar==-1){
                filebr.put(idx,new InputStreamReader (new FileInputStream(filelist[idx])));
            }
            if(num<sperate-1){
                for(int i=num;i<sperate;i++){
                    line[i]=line[num-1];
                }
            }
            for (int i = windos; i < line.length - windos; i++) {
                Vector input = new Vector(mod.getSize(),0);
                if (mod.getVector(line[i])!=null) {
                    input = mod.getVector(line[i]);
                }
                else{
                    System.out.println("发现单词"+line[i]+"没有对应向量");
                }
                subcorpus.put(substring(line, i - windos, i + windos+1), input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subcorpus;
    }

    public HashMap<List<String>,Vector> handlesent_cbow_cn(Model mod,int windos){
        HashMap<List<String>,Vector> subcorpus=new HashMap<List<String>,Vector>();
        try {
            String tempchar;
            String term="";
            int num=0;
            int idx=(int)(Math.random()* filebr_cn.size());
            BufferedReader br= filebr_cn.get(idx);
            tempchar=br.readLine();
            String sentence="";
            while(tempchar!=null && num<10){
                tempchar=br.readLine();
                sentence+=tempchar.trim();
                num++;
            }
            if(tempchar==null) {
                filebr_cn.put(idx, new BufferedReader(new InputStreamReader(new FileInputStream(filelist[idx]))));
            }
            List<SegToken> words=segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);
            String[] line=new String[words.size()];
            for(int i=0;i<line.length;i++){
                line[i]=words.get(i).word;
            }
            for (int i = windos; i < line.length - windos; i++) {
                Vector input = new Vector(mod.getSize(),0);
                for (int j = i - windos; j < i + windos; j++) {
                    if (j == i) {
                        continue;
                    }
                    Vector tmp=mod.getVector(line[j]);
                    if (tmp!=null) {
                        input = Vector.adds(input,tmp);
                    }else{
                        System.out.println("发现单词"+line[j]+"没有对应向量");
                    }
                }
                subcorpus.put(substring(line, i - windos, i + windos+1), input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subcorpus;
    }

    public HashMap<List<String>,Vector> handlesent_sg_cn(Model mod,int windos){
        HashMap<List<String>,Vector> subcorpus=new HashMap<List<String>,Vector>();
        try {
            String tempchar;
            String term="";
            int num=0;
            int idx=(int)(Math.random()* filebr_cn.size());
            BufferedReader br= filebr_cn.get(idx);
            tempchar=br.readLine();
            String sentence="";
            while(tempchar!=null && num<10){
                tempchar=br.readLine();
                sentence+=tempchar.trim();
                num++;
            }
            if(tempchar==null) {
                filebr_cn.put(idx, new BufferedReader(new InputStreamReader(new FileInputStream(filelist[idx]))));
            }
            List<SegToken> words=segmenter.process(sentence, JiebaSegmenter.SegMode.INDEX);
            String[] line=new String[words.size()];
            for(int i=0;i<line.length;i++){
                line[i]=words.get(i).word;
            }
            for (int i = windos; i < line.length - windos; i++) {
                Vector input = new Vector(mod.getSize(),0);
                for (int j = i - windos; j < i + windos; j++) {
                    if (j == i) {
                        continue;
                    }
                    Vector tmp=mod.getVector(line[j]);
                    if (tmp!=null) {
                        input = Vector.adds(input,tmp);
                    }else{
                        System.out.println("发现单词"+line[j]+"没有对应向量");
                    }
                }
                subcorpus.put(substring(line, i - windos, i + windos+1), input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return subcorpus;
    }

    private List<String> substring(String[] ob,int start,int end){
        List<String> out=new ArrayList<String>();
        for(int i=0;i<end-start;i++){
            out.add(ob[i+start]);
//            System.out.print(ob[i+start]+"\t");
        }
//        System.out.print("\n");
        return out;
    }


}
