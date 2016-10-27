import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Main {
	
	public static void main(String[] args) throws IOException, ParseException {
//		LocalTime lt = new LocalTime(22, 4, 5, 0);
//		LocalTime t1 = new LocalTime(1, 0, 0, 0);
//		LocalTime t2 = new LocalTime(7, 13, 0, 0);
//		Period pd = new Period(t1, t2);
//		Period pd2 = new Period(t1, lt);
//		System.out.println("pd " + pd.toString());
//		System.out.println("pd2 " + pd2.toString());
//		pd = pd.plus(pd2);
//		System.out.println(pd.toString());
//		lt = lt.plus(new Period(t1, t2));
//		System.out.println(lt.toString());
		for(int i=0; i<args.length; i++) {
			//System.out.println(args[i]);
		}
		Simulator.simulator.setAlgorithm(Integer.parseInt(args[0]));
		Simulator.simulator.setAskingRate(Integer.parseInt(args[1]));
		Simulator.simulator.random_seed = Integer.parseInt(args[2]);
		Simulator.simulator.predictability = Double.parseDouble(args[3]);
		Simulator.simulator.num_of_expertises = Integer.parseInt(args[4]);
		Simulator.simulator.getWholeGraph();
		Simulator.simulator.usersAskQuestion();
		Simulator.simulator.createOutputFiles();
		Simulator.simulator.run();
	}
	

//	static ArrayList<Question> questionList;
//	static ArrayList<Question> completedQuestionList;
//	
//	static int isResourceObjIndexFromFile=1;
//	static String indexFileName="resourceObjIndex.txt";
//	static ArrayList<Integer> resourceObjIndexList;
//
//	static int preUncompletedSize=0,scheduleCounter=0;
//	static int MaxQueuingDays=1;
//	static int Days=20*MaxQueuingDays;
//	static int Moores=1;
//	static int curT=0, step=1; //one hour
//	static int affordableJobLen=24*60*60; //second (prevent too big job)
//	
//	static PriorityQueue <Question> uncompletedQuestionQueue,  packagedUncompletedQuestionQueue;
//	static int isMiddleOfRound=1,isAllQuestionsInQueue=0, startingDayPerRound=0;
//
//	//for output statistics
//	static Writer outCompletedQuestionID, outAllQuestionResponseTime, outAllQuestionMakespan,outAllQuestionRescheduledNum,
//	outNumCompletedQuestion, outErrorRate, outNumResubmit;
//	static Writer poissonWriter,indexWriter;
//	static int numRescheduledQuestions=0;//per day
//	static int totalEstimatedCompletionTime=0;//per day
//	static int averageND=0; //Normalized Deviation
// 
//	
//
//	static int todayFirstJobID=0, preFirstJobID=0;
//	static int todayFirstFogDeviceID=0;
//	
//	public static void main(String[] args) throws IOException, InterruptedException {
//		String whichRound = args[0];
//		Days = Integer.parseInt(args[1]);
//		affordableJobLen = Integer.parseInt(args[2])*60*60;
//		
//		questionList=new ArrayList<Question>();
//		completedQuestionList=new ArrayList<Question>();
//		
//		//create output file deiscriptor
//		
//		outNumCompletedQuestion = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("NumCompletedQuestion_"+whichRound), "utf-8"));
//		outNumResubmit= new BufferedWriter(new OutputStreamWriter(new FileOutputStream("NumResubmit_"+whichRound), "utf-8"));
//		outCompletedQuestionID =new BufferedWriter(new OutputStreamWriter(new FileOutputStream("CompletedQuestionID_"+whichRound), "utf-8"));
//		outAllQuestionMakespan=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("AllQuestionMakespan_"+whichRound), "utf-8"));
//		outAllQuestionResponseTime=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("AllQuestionProcessingTime_"+whichRound), "utf-8"));
//		outAllQuestionRescheduledNum=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("AllQuestionRescheduledNum_"+whichRound), "utf-8"));
//		outErrorRate=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("ErrorRate_"+whichRound), "utf-8"));
//
//		//check if some randomized inputs are exists or need to be recreated
//		
//		if (isResourceObjIndexFromFile==1){
//			readResourceObjIndex(indexFileName);
//		}else{
//			indexWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(indexFileName), "utf-8"));
//			indexWriter.write("0");
//		}
//		
//		//Comparator<Question> comparator = new JobTimeComparator();
//		uncompletedQuestionQueue = new PriorityQueue<Question>(questionList.size(),comparator);
//		packagedUncompletedQuestionQueue = new PriorityQueue<Question>(questionList.size(),comparator);
//		int questionCounter=0,preQuestionCounter=0;
//		System.out.println("totalJobNUM:"+questionList.size());
//		Question q = questionList.get(questionCounter);// pop up a job
//		q.id=questionCounter;
//		questionCounter+=1;
//		int daycounter=0;
//		while (true)
//		{
//			//terminate
//			if (completedQuestionList.size() == questionList.size()) break; //finish all the jobs
//			if (curT > Days*24) break; //simulation time is up	
//
//			while ((curT-q.timeInterval.startingTime)>0 && isAllQuestionsInQueue==0) { //deal with multiple jobs arrive at the same time
//				uncompletedQuestionQueue.add(q);
//				// pop up a new job
//				q = questionList.get(questionCounter);
//				q.id=questionCounter;
//				questionCounter+=1;
//				if (questionCounter>=questionList.size())isAllQuestionsInQueue=1;
//			}
//			//check if any fog devices arrives, and add devices in to list
//			
//			curT+=step; // one hour
//			//System.out.println("CurT:"+curT+",Day:"+daycounter);
//			daycounter=curT/24;
//			if (daycounter-startingDayPerRound>=MaxQueuingDays) isMiddleOfRound=0;
//			
//			if (curT%(24)==0){
//				//System.out.println(jobCounter-preJobCounter);
//				preQuestionCounter=questionCounter;
//			}
//			// #####  process the jobs at 0:00 a.m. ####
//			
//			if (daycounter>1){ // from third day (second day's jobs are finished)
//				//#####################################
//				//#####################################
//				//output statistics in the end of a day
//				if(curT%(24)==0 && isMiddleOfRound==0){ //end of a day
//					for (int i=0;i<completedQuestionList.size();i++){
//						Question question = completedQuestionList.get(i);
//						outCompletedQuestionID.write((question.id+1)+"");
//						if(i < completedQuestionList.size()-1){
//							outCompletedQuestionID.write(",");
//						}
//					}
//					//System.out.println("preJID:"+preFirstJobID+", tJID:"+todayFirstJobID+", JC:"+jobCounter);
//					for (int i=preFirstQuestionID;i<todayFirstQuestionID-1;i++){
//						Question question = questionList.get(i);
//						
//						outAllQuestionResponseTime.write(question.realRenderingTime+"");
//						outAllQuestionRescheduledNum.write(question.rescheduledNum+"");
//						//outErrorRate.write((float)(question.estimatedRenderingTime-question.idealRenderingTime)/(float)job.idealRenderingTime+"");
//						if(i < questionCounter-1){
//							
//							outAllQuestionMakespan.write(",");
//							outAllQuestionResponseTime.write(",");
//							outAllQuestionRescheduledNum.write(",");
//							outErrorRate.write(",");
//						}
//					}
//					preFirstJobID=todayFirstJobID;
//					System.out.println("Day:"+daycounter+",completedJobNum:"+(completedQuestionList.size()));
//					System.out.println("num of resubmit Jobs:"+numRescheduledQuestions);
//					outNumCompletedQuestion.write((completedQuestionList.size())+"");
//					
//					totalEstimatedCompletionTime=0;//reset the value
//					outNumResubmit.write(numRescheduledQuestions+"");
//					numRescheduledQuestions=0;
//					if(curT/(24*60*60)<Days){
//						if (completedQuestionList.size()>0)outCompletedQuestionID.write("\n");
//						else outCompletedQuestionID.write(",\n");
//						
//						outErrorRate.write("\n");
//						outAllQuestionMakespan.write("\n");
//						outAllQuestionResponseTime.write(",");
//						outAllQuestionRescheduledNum.write("\n");
//						outNumCompletedQuestion.write(",");
//						outNumResubmit.write(",");
//					}
//
//					completedQuestionList=new ArrayList<Question>();
//				}
//			}
//				//###########
//				// prepare for schedule
//				//###########
//			if (daycounter>0){ // from second day
//				if (curT%(24)==0){ //0:00 a.m.
//					if (isMiddleOfRound==0){//next round (1. finished pre-round's jobs)
//						startingDayPerRound=daycounter;
//						scheduleCounter=0;
//						preUncompletedSize=questionList.size();
//						if (uncompletedQuestionQueue.size()>0){
//							packagedUncompletedQuestionQueue = new PriorityQueue<Question>(questionList.size(),comparator);
//							packagedUncompletedQuestionQueue.addAll(uncompletedQuestionQueue);	
//							uncompletedQuestionQueue = new PriorityQueue<Question>(questionList.size(),comparator); //clean uncompletedJobQueue
//							todayFirstJobID=preFirstJobID+packagedUncompletedQuestionQueue.size();
//							isMiddleOfRound=1;
//							System.out.println("Day:"+daycounter+",packagedNumOfJobs:"+packagedUncompletedQuestionQueue.size());
//						}else{//no incoming jobs and finished
//							packagedUncompletedQuestionQueue = new PriorityQueue<Question>(questionList.size(),comparator);
//						}
//					}
//				}
//				//start scheduling
//				//if (newFogDeviceArrive==1){
//				if (Main.packagedUncompletedQuestionQueue.size()!=0){
//					System.out.println("Day:"+daycounter+",Predictor:"+predictor+",ScheduleCounter:"+scheduleCounter+",curT:"+Main.curT+",packagedSize:"+Main.packagedUncompletedJobQueue.size()+",preUncompletedSize:"+preUncompletedSize);
//					//fp.EarliestStartScheduling();
//					
//					scheduleCounter++;
//				}
//				//process 
//				for (int i=0; i<fp.fogDeviceList.size();i++){
//					FogDevice fd = fp.fogDeviceList.get(i);
//					int isDeviceOnline=fd.processJobs();
//					if(isDeviceOnline==0){
//						fp.fogDeviceList.remove(i);
//					}
//				}
//			}
//		}		
//		System.out.println("totalJobs:"+jobCounter);
//		int totalTime=0;
//		int idealTime=0;
//		for (int i=0;i<completedJobList.size();i++){
//			Job job = completedJobList.get(i);
//			totalTime+=job.realRenderingTime;
//			idealTime+=job.idealRenderingTime;
//		}
//		System.out.println("totalTime:"+totalTime);
//		System.out.println("idealTime:"+idealTime);
//		if (isPoissonFromFile==0) poissonWriter.close();
//		if (isResourceObjIndexFromFile==0) indexWriter.close();
//		//close the output descriptors
//		outAllJobCPU.close();
//		outAllJobRAM.close();
//		outNumCompletedJob.close();
//		outNumResubmit.close();
//		outCompletedJobID.close();
//		outAllJobMakespan.close();
//		outAllJobProcessingTime.close();
//		outAllJobRescheduledNum.close();
//		outErrorRate.close();
//		outCompletedJobFrameNum.close();
//	}
//	public static int getPoissonRandom(double mean) {
//		Random r = new Random();
//		double L = Math.exp(-mean);
//		int k = 0;
//		double p = 1.0;
//		do {
//			p = p * r.nextDouble();
//			k++;
//		} while (p > L);
//		return k - 1;
//	}
//	public static void readPossionArrivalTime(String filename) throws FileNotFoundException, IOException{
//		try(BufferedReader br = new BufferedReader(new FileReader(new File(filename)))) {
//			String [] arrivalArr=br.readLine().split(",");
//			for (int i=0;i<arrivalArr.length;i++) fogDeviceArrivalTimeList.add(Integer.parseInt(arrivalArr[i]));
//		}
//	}
//	public static void readResourceObjIndex(String filename) throws FileNotFoundException, IOException{
//		try(BufferedReader br = new BufferedReader(new FileReader(new File(filename)))) {
//			String [] indexArr=br.readLine().split(",");
//			for (int i=0;i<indexArr.length;i++) resourceObjIndexList.add(Integer.parseInt(indexArr[i]));
//		}
//	}
}
