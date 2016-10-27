import sys
import numpy

fin1 = open('../results/questions/Bottleneck/Bottleneck_%s_%s_%s_%s_question_info.txt' % (sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4]))
fin2 = open('../results/questions/iASK/iASK_%s_%s_%s_%s_question_info.txt' % (sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4]))
fin3 = open('../results/questions/SOS/SOS_%s_%s_%s_%s_question_info.txt' % (sys.argv[1], sys.argv[2], sys.argv[3], sys.argv[4]))

bottle = []
for line in fin1:
	lineArr = line.strip().split(",")
	lineArr[4] = lineArr[4].replace("[", "")
	lineArr[4] = lineArr[4].replace("]", "")
	if lineArr[4] == "": 
		continue
	response_time = lineArr[4].split(" ")
	lis = []
	sorted(response_time)
	for t in response_time:
		lis.append(long(t))
	bottle.append(lis)

iask = []
for line in fin2:
	lineArr = line.strip().split(",")
	lineArr[4] = lineArr[4].replace("[", "")
	lineArr[4] = lineArr[4].replace("]", "")
	if lineArr[4] == "":
		continue
	response_time = lineArr[4].split(" ")
	lis = []
	sorted(response_time)
	for t in response_time:
		lis.append(long(t))
	iask.append(lis)

sos = []
for line in fin3:
	lineArr = line.strip().split(",")
	lineArr[4] = lineArr[4].replace("[", "")
	lineArr[4] = lineArr[4].replace("]", "")
	if lineArr[4] == "":
		continue
	response_time = lineArr[4].split(" ")
	lis = []
	sorted(response_time)
	for t in response_time:
		lis.append(long(t))
	sos.append(lis)
bottle_array = numpy.array(bottle)
iask_array = numpy.array(iask)
sos_array = numpy.array(sos)

print "bottleneck: "
arr = numpy.mean(bottle, axis = 1)
arr_std = numpy.std(bottle, axis = 1)

bottle_mean, bottle_std = zip(*sorted(zip(arr, arr_std)))
print arr.size
print arr_std.size
print repr(bottle_mean)

print "\n\n\nbottleneck std:"

print repr(bottle_std)
print "iask: " 
arr = numpy.mean(iask_array, axis = 1)
arr_std = numpy.std(iask_array, axis = 1)
iask_mean, iask_std = zip(*sorted(zip(arr, arr_std)))
print repr(iask_mean)

print "\n\n\niask std:"
print repr(iask_std)
print "sos: "
arr = numpy.mean(sos_array, axis = 1)
arr_std = numpy.std(sos_array, axis = 1)
sos_mean, sos_std = zip(*sorted(zip(arr, arr_std)))
print repr(sos_mean)

print "\n\n\nsos std:"
print repr(sos_std)
