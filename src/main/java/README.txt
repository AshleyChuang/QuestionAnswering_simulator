run simulator:
>> javac *.java
>> java Main arg1 arg2 arg3 arg4 arg5

** arg1: which algorithm to use (0:Bottleneck, 1:iASK, 2: SOS)
** arg2: # of questions 
** arg3: the seed for generate questions randomly
** arg4: active time
** arg5: the number of answerers

output files:

1) [AlgorithmType]_[arg2]_[arg3]_[arg4]_user_info,txt:
[user id, total # of passing, total # of answering, average # of passing, average # of answering, # of friends the user passes to]

2) [AlgorithmType]_[arg2]_[arg3]_[arg4]_question_info.txt:
[question id, asker id, # of being passed, # of being answered, [a list of time that every answerer spent]]

3) [AlgorithmType]_[arg2]_[arg3]_[arg4]_log.txt:
[question id, passer or answerer, user id, timestamp(second)]
