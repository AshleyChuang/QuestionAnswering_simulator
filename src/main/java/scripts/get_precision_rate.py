import os
import sys
path = "../results/questions/%s/" % (sys.argv[1])

for filename in os.listdir(path):
	if filename.endswith(".txt"):
		fin = open('../results/questions/%s/%s' % (sys.argv[1], filename))
		file_info = filename.strip().split("_")
		num_of_answerers = file_info[4]
		num_of_questions = 0.0
		num_of_invalid = 0.0
		num_of_valid = 0.0
		for line in fin:
			num_of_questions += 1
			lineArr = line.strip().split(",")
			num_of_invalid += int(lineArr[5])
			lineArr[4] = lineArr[4].replace("[", "")
			lineArr[4] = lineArr[4].replace("]", "")
			response_time = lineArr[4].split(" ")
			num_of_valid += len(response_time)
		#print "valid: " + str(num_of_valid)
		#print "invalid: " + str(num_of_invalid)
		#print "num_of_answerers: " + str(num_of_answerers)
		#print "num_of_questions: " + str(num_of_questions)
		total = (float)(num_of_answerers) * num_of_questions
		#print "total: " + str(total)
		print num_of_valid/total
