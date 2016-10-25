import os
import sys
path = "results/questions/%s/" % (sys.argv[1])

for filename in os.listdir(path):
	if filename.endswith(".txt"):
		fin = open("results/questions/%s/%s" % (sys.argv[1], filename))
		for line in fin:
			lineArr = line.strip().split(",")
			lineArr[4] = lineArr[4].replace("[", "")
			lineArr[4] = lineArr[4].replace("]", "")
			response_time = lineArr[4].split(" ")
			#print response_time
			shortest = response_time[0]
			longest = response_time[-1]
		#print "shortest: " + shortest
		#print "longest: " + longest
		print int(longest) - int(shortest)
