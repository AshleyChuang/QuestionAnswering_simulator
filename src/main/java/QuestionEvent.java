import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class QuestionEvent extends Event {
	Question question;
	Node asker;
	Set<Node> answerers;
	List<ArrayList<Node>> all_paths;
	Set<Node> subGraph;
	//Node asker;
	
	public QuestionEvent(Question question, long time){
		this.question = question;
		this.time = time;
		this.asker = question.asker;
		//asker = Simulator.simulator.asker;
	}
	
	public void exec(){
		// call Imad's function
		String asker_name = question.asker.userIndex;
		String num_of_answerers = question.num_of_answerers.toString();
		String question_content = question.content;
		String starting_time = question.timeInterval.startingTime.toString();
		String ending_time = question.timeInterval.endingTime.toString();
		String bottleneck_tree = "python /Users/ashley/documents/NMSL/mcloud/doc_infocom17/Scripts/Bottleneck_Tree_Final.py";
		String iASK = "python /Users/ashley/documents/NMSL/mcloud/doc_infocom17/Scripts/iASK_Final.py";
		String SOS = "python /Users/ashley/documents/NMSL/mcloud/doc_infocom17/Scripts/SOS_Final.py";
		String algo = null;
		
		if(Simulator.simulator.algorithm == 0) {
			algo = bottleneck_tree;
		}
		else if(Simulator.simulator.algorithm == 1) {
			algo = iASK;
		}
		else if(Simulator.simulator.algorithm == 2) {
			algo = SOS;
		}
		
		String myCommand = algo
				+ " " + asker_name
				+ " " + num_of_answerers
				+ " '" + question_content + "'"
				+ " " + starting_time
				+ " " + ending_time;
		
		//System.out.println("command: " + myCommand);
		String[] cmd = {
				"/bin/bash",
				"-c",
				myCommand
		};
		try {
			ArrayList<String> output = new ArrayList<String>();
			Process p;
			
			p = Runtime.getRuntime().exec(cmd);

			BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while((line = br.readLine()) != null) {
            	output.add(line);
            }

			p.waitFor();
			p.destroy();
			readResult(output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		asker.getQuestionTime = time;
		asker.getQuestionPeriod = 0L;
		Simulator.simulator.output.println("Asker " + asker.userId + " : " + asker.getQuestionTime);
		deliverQuestion();
		super.exec();
	}
	
	public void deliverQuestion() {
		for(int i=0; i<all_paths.size(); i++) {
			for(int j=0; j<all_paths.get(i).size(); j++) {
				if(j < (all_paths.get(i).size()-2)) {
					Node n = all_paths.get(i).get(j);
					Node m = all_paths.get(i).get(j+1);
					
					Long hours = (n.getQuestionTime % (24 * 60 * 60))/(60*60);
					long edge_delay = -1;
					for(int k=0; k < m.activeTime.size(); k++) {
						if(m.activeTime.get(k) == hours) {
							edge_delay = 0;
							break;
						}
						else if(m.activeTime.get(k) > hours) {
							edge_delay = m.activeTime.get(k) - hours;
							break;
						}
					}
					if(edge_delay == -1) {
						edge_delay = m.activeTime.get(0) + (24 - hours);
					}
					n.pass_to.add(m);
					int basic_edge_delay = 0;
					if(n.edge_delay.containsKey(m.userId)) {
						basic_edge_delay = n.edge_delay.get(m.userId);
					}
					RelayEvent e = new RelayEvent(question, m, n.getQuestionTime+edge_delay*60*60+basic_edge_delay, basic_edge_delay+edge_delay*60*60+n.getQuestionPeriod);
					long days = n.getQuestionTime%(24*60*60);
					if(n.pass_each_day.containsKey(days)) {
						n.pass_each_day.put(days, n.pass_each_day.get(days) + 1);
					}
					else {
						n.pass_each_day.put(days, 1);
					}
					Simulator.simulator.addEvent(e);
				}
				else if (j == all_paths.get(i).size()-2) {
					Node n = all_paths.get(i).get(j);
					Node m = all_paths.get(i).get(j+1);
					Long hours = (n.getQuestionTime % (24 * 60 * 60))/(60*60);
					long edge_delay = -1;
					for(int k=0; k < m.activeTime.size(); k++) {
						if(m.activeTime.get(k) == hours) {
							edge_delay = 0;
							break;
						}
						else if(m.activeTime.get(k) > hours) {
							edge_delay = m.activeTime.get(k) - hours;
							break;
						}
						
					}
					if(edge_delay == -1) {
						edge_delay = m.activeTime.get(0) + (24 - hours);
					}
					n.pass_to.add(m);
					int basic_edge_delay = 0;
					if(n.edge_delay.containsKey(m.userId)) {
						basic_edge_delay = n.edge_delay.get(m.userId);
					}
					AnswerEvent e = new AnswerEvent(question, m, n.getQuestionTime+edge_delay*60*60+basic_edge_delay, basic_edge_delay+edge_delay*60*60+n.getQuestionPeriod);
					long days = n.getQuestionTime%(24*60*60);
					if(n.answer_each_day.containsKey(days)) {
						n.answer_each_day.put(days, n.answer_each_day.get(days) + 1);
					}
					else {
						n.answer_each_day.put(days, 1);
					}
					Simulator.simulator.addEvent(e);
				}
			}
		}
	}

	public void readResult(ArrayList<String> output) throws IOException, ParseException {
		answerers = new HashSet<Node>();
		subGraph = new HashSet<Node>();
		String line; 
		Map<String, Node> nodes_name = new HashMap<String, Node>();
		Map<String, Integer> pair_of_nodes = new HashMap<String, Integer>();
		all_paths = new ArrayList<ArrayList<Node>>();
		Iterator<String> it = output.iterator();
		// create all the nodes and their types
		while(it.hasNext()) {
			line = it.next();
			//System.out.println(line);
			String[] lineArr = line.split(",");
//			
			int begin = line.indexOf('[') + 1;
			int end = line.indexOf(']');
			if(begin==0 && end==-1) {
				Simulator.simulator.no_path_solution ++;
                System.out.println("NO PATH");
				continue;
			}
			String path = line.substring(begin, end);
			
			String[] path_list = path.split(", ");
			List<Node> nodes_in_a_path = new ArrayList<Node>();

			for(int i=0; i<path_list.length; i++) {
				String x = path_list[i];
				x = x.replace("'", "");
				nodes_in_a_path.add(Simulator.simulator.wholeGraph.get(x));
			}
			all_paths.add((ArrayList<Node>) nodes_in_a_path);
		}
        if(Simulator.simulator.no_path_solution == output.size()){
            System.out.println("no solution at all");
            System.exit(1);
        }
    }

}
