import java.util.ArrayList;
import java.util.List;

public class Question {
	Node asker;
	String questionId;
	Integer num_of_answerers;
	String content;
	TimeInterval timeInterval;
	int number_pass;
	int number_answer;
	List<Long> spentTime;
	
	public Question(String questionId, Node asker, Integer num_of_answerers, String content, TimeInterval ti) {
		this.questionId = questionId;
		this.asker = asker;
		this.num_of_answerers = num_of_answerers;
		this.content = content;
		this.timeInterval = ti;
		this.spentTime = new ArrayList<Long>();
		this.number_pass = 0;
		this.number_answer = 0;
	}
	
	public void solved() {
		Simulator.simulator.question_info.print(questionId + "," + asker.userId + "," + Integer.toString(number_pass) + "," +  Integer.toString(number_answer) + ",[");
		for(int i=0; i<num_of_answerers; i++) {
			Simulator.simulator.question_info.print(spentTime.get(i).toString());
			if(i!=(num_of_answerers-1)) {
				Simulator.simulator.question_info.print(" ");
			}
		}
		Simulator.simulator.question_info.print("],");
		Simulator.simulator.question_info.println(Simulator.simulator.no_path_solution);
		Simulator.simulator.no_path_solution = 0;
	}
	
}
