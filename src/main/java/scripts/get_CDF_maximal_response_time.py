import sys
import os

max_b = []
max_iask = []
max_sos = []
path = '../results/questions/'

for i in range(1, 11):
	fin1 = open('../results/questions/Bottleneck/Bottleneck_14400_%s_1.0_4_question_info.txt' % (str(i)))
	fin2 = open('../results/questions/iASK/iASK_14400_%s_1.0_4_question_info.txt' % (str(i)))
	fin3 = open('../results/questions/SOS/SOS_14400_%s_1.0_4_question_info.txt' % (str(i)))
	
	for line in fin1:
		lineArr = line.strip().split(",")
		lineArr[4] = lineArr[4].replace("[", "")
		lineArr[4] = lineArr[4].replace("]", "")
		if(lineArr[4] == ""):
			continue
		response_time = lineArr[4].split(" ")
		max_b.append(float(response_time[-1])/3600)
		
	for line in fin2:
		lineArr = line.strip().split(",")
		lineArr[4] = lineArr[4].replace("[", "")
		lineArr[4] = lineArr[4].replace("]", "")
		if(lineArr[4] == ""):
			continue
		response_time = lineArr[4].split(" ")
		max_iask.append(float(response_time[-1])/3600)
		
	for line in fin3:
		lineArr = line.strip().split(",")
		lineArr[4] = lineArr[4].replace("[", "")
		lineArr[4] = lineArr[4].replace("]", "")
		if(lineArr[4] == ""):
			continue
		response_time = lineArr[4].split(" ")
		max_sos.append(float(response_time[-1])/3600)
print "bottle = " + str(max_b) + ";"
print "iask = " + str(max_iask) + ";"
print "sos = " + str(max_sos) + ";"
