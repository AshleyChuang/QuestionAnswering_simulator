
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class Simulator {
	public static Simulator simulator = new Simulator();
	// simulation time
	long curTime;
	Map<Long, ArrayList<Event>> events; // <Time, Event>
	public Map<String, Node> wholeGraph;
	PrintWriter output;
	PrintWriter question_info;
	PrintWriter user_info;
	static long PoissonArrivalRate=60*60;//sec
	QuestionGenerator questionGenerator;
	List<Question> list_of_question;
	int algorithm = 0; // 0:bottlenect, 1:iASK, 2:SOS
	int random_seed = 0;
	double alpha = 1.0; // {0.6, 0.8, 1}
	int no_path_solution = 0;
	int num_question = 0;
	int num_of_answerers = 4; // {1, 2, 4, 6, 8}
    float user_asking_rate = 1/4; // {1/10, 1/8, 1/4, 1/2, 1}
    int num_of_expertises = 4; // {2, 4, 8, 16}
    
	public Simulator() {
		list_of_question = new ArrayList<Question>();
		wholeGraph = new HashMap<String, Node>();
		try {
			questionGenerator = new QuestionGenerator();
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		events = new TreeMap<Long, ArrayList<Event>>();
	}
	public void run() {
		Iterator<Long> it = events.keySet().iterator();
		
		while(it.hasNext()) {
			long key = it.next();
			curTime = key;
			ArrayList<Event> list_of_events = events.get(key);
			Iterator<Event> it_events = list_of_events.iterator();
			
			while(it_events.hasNext()) {
				Event event = it_events.next();
				event.exec();
				it_events.remove();
			}
			events.remove(key);
			it = events.keySet().iterator();
		}
		output.close();
		this.printQuestionInfo();
		this.printUserInfo();
	}
	public void addEvent(Event event){
		if(events.get(event.time) == null) {
			events.put(event.time, new ArrayList<Event>());
		}
		events.get(event.time).add(event);
	}
	public void generateQuestions(int num) {
		this.num_question = num;
		for(int i=1; i<=num; i++) {
			Question q = questionGenerator.generate(i, wholeGraph.size(), random_seed+1);
			QuestionEvent question_event = new QuestionEvent(q, q.timeInterval.startingTime*3600);
			if(events.get(question_event.time) == null) {
				events.put(question_event.time, new ArrayList<Event>());
			}
			events.get(question_event.time).add(question_event);
			list_of_question.add(q);
		}
	}
	public void printQuestionInfo() {
		Iterator<Question> it = list_of_question.iterator();
		
		while(it.hasNext()) {
			Question q = it.next();
			q.solved();
		}
		this.question_info.close();
	}
	public void printUserInfo() {
		Iterator it = wholeGraph.entrySet().iterator();
	    while (it.hasNext()) {
	    	Map.Entry pair = (Map.Entry)it.next();
	        ((Node)pair.getValue()).writeToOutput();
	    }
	    this.user_info.close();
	}
	public void getWholeGraph() throws IOException, ParseException {
		BufferedReader br = null;
		BufferedReader br_ad = null;
		String line;
		String[] str;
		Map<String, Node> nodes_time = new HashMap<String, Node>();
		try {
			br = new BufferedReader(new FileReader("1_2_hop_time.csv"));
			br_ad = new BufferedReader(new FileReader("1_2_hop_adjacencylist.csv"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
		    line = br.readLine();
		    while (line != null) {
		        str = line.split(",");
 		        Node n = new Node();
		        n.userId = str[0];
		        int k = 0;
		        for(int i=1; i<str.length; i++) {
		        	if(k%2==1) {
		        		k++;
		        		continue;
		        	}
		        	Long a_t = Long.parseLong(str[i]);
		        	double rand = Math.random();
		        	if(rand<=alpha) {
		        		n.activeTime.add(a_t);
		        	}
		        	k++;
				}
		        nodes_time.put(str[0], n);
		        //wholeGraph.put(str[0], n);
		        line = br.readLine();
		    }
		    //System.out.println("size: " + wholeGraph.size());
		} finally {
		    br.close();
		}
		Integer i=0; 
		while((line = br_ad.readLine()) != null) {
			str = line.split(",");
			if(nodes_time.containsKey(str[0])) {
				Node n = nodes_time.get(str[0]);
				n.userIndex = i.toString();
				wholeGraph.put(str[0], n);
				i++;
				for(int j=1; j<str.length; j++) {
					String[] edge_delay = str[j].split(": ");
					if(nodes_time.containsKey(edge_delay[0])) {
						n.edge_delay.put(edge_delay[0], Integer.parseInt(edge_delay[1]));
					}
				}
			}
		}
		
		
	}
	
	public void createOutputFiles() {
		try {
			StringBuilder file_name = new StringBuilder();
			
			if(this.algorithm == 0) file_name.append("Bottleneck/").append("Bottleneck_").append(this.num_question).append("_").append(this.random_seed).append("_").append(this.alpha);
			else if(this.algorithm == 1) file_name.append("iASK/").append("iASK_").append(this.num_question).append("_").append(this.random_seed).append("_").append(this.alpha);
			else if(this.algorithm == 2) file_name.append("SOS/").append("SOS_").append(this.num_question).append("_").append(this.random_seed).append("_").append(this.alpha);
		
			output = new PrintWriter("results/log/" + file_name.toString() + "_log.txt");
			question_info = new PrintWriter("results/questions/" + file_name.toString() + "_question_info.txt");
			user_info = new PrintWriter("results/users/" + file_name.toString() + "_user_info.txt");
		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public void setAlgorithm(int i) {
		this.algorithm = i;
	}
}
