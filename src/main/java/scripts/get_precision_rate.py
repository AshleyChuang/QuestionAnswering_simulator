import os
import sys
path = "results/questions/%s/" % (sys.argv[1])

for filename in os.listdir(path):
	if filename.endswith(".txt"):
		fin = open('results/quesitons/%s/%s' % (sys.argv[1], filename))
		for line in fin:
			lineArr = line.strip().split(",")
			invalid = int(lineArr[5])
			lineArr[4] = lineArr[4].replace("[", "")
			lineArr[4] = lineArr[4].replace("]", "")
			response_time = lineArr[4].split(" ")
			valid = len(response_time)
			print float(valid)/float(lineArr[3])
			if (valid+invalid) == int(LineArr[3]):
				print "CORRECT!"

