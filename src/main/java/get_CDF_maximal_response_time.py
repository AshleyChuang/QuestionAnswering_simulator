import sys


max_b = []
max_iask = []
max_sos = []
for i in range(1, 11):
	fin1 = open('results/questions/Bottleneck/Bottleneck_1_%s_1.0_question_info.txt' % str(i))
	fin2 = open('results/questions/iASK/iASK_1_%s_1.0_question_info.txt' % str(i))
	fin3 = open('results/questions/SOS/SOS_1_%s_1.0_question_info.txt' % str(i))
	
	for line in fin1:
		lineArr = line.strip().split(",")
		lineArr[4] = lineArr[4].replace("[", "")
		lineArr[4] = lineArr[4].replace("]", "")
		response_time = lineArr[4].split(" ")
		max_b.append(float(response_time[-1])/3600)
		
	for line in fin1:
		lineArr = line.strip().split(",")
		lineArr[4] = lineArr[4].replace("[", "")
		lineArr[4] = lineArr[4].replace("]", "")
		response_time = lineArr[4].split(" ")
		max_b.append(float(response_time[-1])/3600)
		
	for line in fin2:
		lineArr = line.strip().split(",")
		lineArr[4] = lineArr[4].replace("[", "")
		lineArr[4] = lineArr[4].replace("]", "")
		response_time = lineArr[4].split(" ")
		max_iask.append(float(response_time[-1])/3600)
		
	for line in fin3:
		lineArr = line.strip().split(",")
		lineArr[4] = lineArr[4].replace("[", "")
		lineArr[4] = lineArr[4].replace("]", "")
		response_time = lineArr[4].split(" ")
		max_sos.append(float(response_time[-1])/3600)
print str(max_b) + ";"
print str(max_iask) + ";"
print str(max_sos) + ";"
