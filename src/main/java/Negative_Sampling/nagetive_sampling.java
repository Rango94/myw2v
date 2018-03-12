package Negative_Sampling;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import Modelhandler.Vector;
import java.util.Set;

public class nagetive_sampling {
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
    public HashMap<String,Integer> dictionary=new HashMap<String, Integer>();
    private List<String> stopwords=new ArrayList<String>();
    public int totalnum=0;
    public List<String> lowwords=new ArrayList<String>();
    private List<Double> line_location=new ArrayList<Double>();
    private List<String> line_name=new ArrayList<String>();
    private HashMap<String,Vector> termvector_ass =new HashMap<String, Vector>();
    private int vectorsize;
    private List<String> Termlist=new ArrayList<String>();



    public nagetive_sampling(String corpuspath,int size){
        vectorsize=size;
        for(String e:stopwords_str){
            stopwords.add(e);
        }
        readcorpus(corpuspath);
        genareteline(dictionary);
    }

    public Set<String> getTermlist(){
        return dictionary.keySet();
    }
    public List<String> getsampling(String term,int size){
        List<String> out=new ArrayList<String>();
        for(int i=0;i<size;i++){
            String e=getsinglesampling();
            if(e.equals(term)){
                i--;
            }else {
                out.add(e);
            }
        }
        System.out.println();
        return out;
    }

    public Vector getVector(String name){
        return termvector_ass.get(name);
    }

    private String getsinglesampling(){
        double idx=Math.random();
//        System.out.print(Double.toString(idx)+'\t');
        int idx_int=(int)(line_location.size()*idx);
        if (line_location.get(idx_int)>idx){
            while(line_location.get(idx_int)>idx) {
                idx_int -= 1;
            }
            System.out.print(Double.toString(line_location.get(idx_int))+'\t'+Double.toString(line_location.get(idx_int+1))+"\t"+line_name.get(idx_int+1)+"\n");
            return line_name.get(idx_int+1);
        }else{
            while(line_location.get(idx_int)<=idx) {
                idx_int += 1;
            }
            System.out.print(Double.toString(line_location.get(idx_int-1))+'\t'+Double.toString(line_location.get(idx_int))+"\t"+line_name.get(idx_int-1)+"\n");
            return line_name.get(idx_int-1);
        }
    }


    private void genareteline(HashMap<String,Integer> dictionary){
        HashMap<String,Double> tmp_dic=new HashMap<String, Double>();
        double tmptotal=0;
        for(String e:dictionary.keySet()){
            tmptotal+=Math.pow(dictionary.get(e),0.75);
        }
        double tmploca=0;
        for(String e:dictionary.keySet()){
            tmploca+=Math.pow(dictionary.get(e),0.75)/tmptotal;
            line_location.add(tmploca);
            line_name.add(e);
        }
    }



    private HashMap<String, Integer> readcorpus(String corpuspath) {
        try {
            Reader br = new InputStreamReader(new FileInputStream( new File(corpuspath)));
            int tempbyte;
            String term="";
            while ((tempbyte = br.read()) != -1) {
                char w=(char)tempbyte;
                if(Character.isSpaceChar(w)||tempbyte==10||tempbyte==13){
                    if(term!=""&& !stopwords.contains(term)){
                        totalnum++;
                        if (dictionary.containsKey(term)) {
                            dictionary.put(term, dictionary.get(term) + 1);
                        } else {
                            termvector_ass.put(term,new Vector(vectorsize));
                            dictionary.put(term, 1);
                        }
                    }
                    term="";
                }else {
                    term += String.valueOf(w);
                }
            }
            br.close();
            lowwords=new ArrayList<String>();
            for(String e:dictionary.keySet()){
                if(dictionary.get(e)<10){
                    lowwords.add(e);
                }
            }
            for(String e:lowwords){
                dictionary.remove(e);
                termvector_ass.remove(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dictionary;
    }

}
