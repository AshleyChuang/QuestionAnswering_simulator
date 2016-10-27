import re, math

import numpy as np

import numpy.linalg as linalg

from collections import Counter

import networkx as nx

import matplotlib.pyplot as plt

from copy import copy

import csv

import re

import json

import sys

fin_posts = open('1_2_hop_post.csv')

fin_active_time = open('1_2_hop_time.csv')

fin_adjacency_list = open('1_2_hop_adjacencylist.csv')

asker = sys.argv[1]
number_of_answerers = sys.argv[2]
question = sys.argv[3]
start_time = sys.argv[4]
end_time = sys.argv[5]

time = []



nodes = []

v = []



net = {}

nodes_with_posts = {}



for line in fin_adjacency_list:

    lineArr = line.strip().split(",")

    name = lineArr[0]

    nodes.append(name)

    nodes_with_posts[name] = []

    friends_edge_delay = {}

    for i in range(1, len(lineArr)):

        friend_delay = lineArr[i].strip().split(":")

        friend_name = friend_delay[0]

        edge_delay = friend_delay[1]

        friends_edge_delay[friend_name] = int(edge_delay)

    net[name] = friends_edge_delay

#print net



fin_posts = open('1_2_hop_post.csv')

for line in fin_posts:

    lineArr = line.strip().split(",")

    name = lineArr[0]

    if name not in nodes:

        continue

    user_posts = []

    for i in range(1, len(lineArr)):

        user_posts.append([lineArr[i]])

        #print user_posts

    nodes_with_posts[name] = user_posts



for node in nodes_with_posts:

    v.append(nodes_with_posts[node])



for line in fin_active_time:

    lineArr = line.strip().split(",")

    if lineArr[0] not in nodes:

        continue

    user_active_time = []

    for i in range(1, len(lineArr)):

        user_active_time.append(int(lineArr[i]))

    time.append(user_active_time)
   


#function for finding nearest interval

def nearesttime(time,nodes,check,minNode,s):

##    print minNode

##    print s

    templ=[]

    timestring=' '

    if len(time[nodes.index(minNode)])>2: #if multiple intercvals


        for k in range(0,len(time[nodes.index(minNode)]),1):

##            print k

##            print 'hi'

            if k%2==0:

                templ.append(abs(time[nodes.index(s)][0] - time[nodes.index(minNode)][k]))

                timestring=timestring+',('+str(time[nodes.index(minNode)][k])

            else:

                templ.append(1000)

                timestring=timestring+'-'+str(time[nodes.index(minNode)][k])+')'


            minimumdiff=min(j for j in templ if j !=1000)

            pos=templ.index(minimumdiff)

                    #newneighbour is nearest interval

        newneighbor=[time[nodes.index(minNode)][pos],time[nodes.index(minNode)][pos+1]] # nearest time interval calculated



    else:

        newneighbor=[time[nodes.index(minNode)][0],time[nodes.index(minNode)][1]] # if single interval return that one

    #print '_______________________________________'

    return newneighbor


#function for finding shortest path with intervals

def shortpath_withintervals(net, time,nodes,check,s, t):



    # sanity check

    #if s == t:

        #return "The start and terminal nodes are the same. Minimum distance is 0."

    if net.has_key(s)==False:



        return "There is no start node called " + str(s) + "."

    if net.has_key(t)==False:

        return "There is no terminal node called " + str(t) + "."

    # create a labels dictionary

    labels={}

    newlabel={}

    # record whether a label was updated


    order={}

    # for saving node and edge time

    edge=[]

    nodetime={}

    edgetime={}

    # populate an initial labels dictionary

    for i in net.keys():

        if i == s: labels[i] = 0 # shortest distance form s to s is 0

        else: labels[i] = float("inf") # initial labels are infinity



    from copy import copy

    drop1 = copy(labels) # used for looping

    ## begin algorithm

    firsttime=1

    checkagain=s



    while len(drop1) > 0:

        #print len(drop1)

        # find the key with the lowest label

        minNode = min(drop1, key = drop1.get) #minNode is the node with the smallest value / drop1.get gives us values . in which we then the minimum key

        newneighbor=nearesttime(time,nodes,check,minNode,s)

        caltime=0

        # update labels for nodes that are connected to minNode

        for i in net[minNode]:

            if labels[i] > (labels[minNode] + net[minNode][i]+caltime):

                #finding nearest time interval if multiple intervals

                newneighbor=nearesttime(time,nodes,check,i,minNode)

                #print newneighbor

                    ########################################################

                if time[nodes.index(minNode)][0]<=newneighbor[1] and newneighbor[0]<=time[nodes.index(minNode)][1]: #if overlaping

                    caltime=0

                    nodetime[i]=0

                else: #adding time with weight if not overlaping

                    if newneighbor[0]==time[nodes.index(minNode)][0]:

                        caltime=0

                    else:



                        if newneighbor[0]<time[nodes.index(minNode)][0]:

                            caltime=(24+newneighbor[0]-time[nodes.index(minNode)][0])*3600 # Change it to 60 if adjacent nodes timings are in minutes.

                        else:

                            caltime=(abs(newneighbor[0]-time[nodes.index(minNode)][0])*3600) # Change it to 60 if adjacent nodes timings are in minutes.

                #print caltime


                labels[i] = labels[minNode]+net[minNode][i]+caltime

                newlabel[i]= labels[minNode]+net[minNode][i]+caltime

                drop1[i] = labels[minNode] + net[minNode][i]+caltime #add the node to be added

                order[i] = minNode #assign value to du for comparison

                edgetime[i]=labels[i]


                #print labels[i]

                 #find edge for appending

                for key,value in net.iteritems():

                    if key==minNode:

                        for k,val in value.iteritems():

                            if val==net[minNode][i]:

                                edge.append(k)

            #print '-----------------------'

        del drop1[minNode] # once a node has been visited, it's excluded from drop1

        #print drop1

        #print '+++++++++++++++++++++++++++++++++++++++++'

    ## end algorithm

    # print shortest path

    temp = copy(t)

    rpath = []

    path = []

    while 1:

        rpath.append(temp)

        if order.has_key(temp): temp = order[temp]

        else: return "There is no path from " + str(s) + " to " + str(t) + "."

        if temp == s:

            rpath.append(temp)

            break

    for j in range(len(rpath)-1,-1,-1):

        path.append(rpath[j])

    return ("Actual path from " + s + " to " + t + " is " + str(path) + ". Distance on this path with interval is " + str(newlabel[t]) + ".")


#function for finding shortest path without intervals and distance on that path with intervals

def shortest_path_without_interval(net,check, s, t):



    # sanity check

    if s == t:

        return "The start and terminal nodes are the same. Minimum distance is 0."

    if net.has_key(s)==False:

        return "There is no start node called " + str(s) + "."

    if net.has_key(t)==False:

        return "There is no terminal node called " + str(t) + "."

    # create a labels dictionary




    labels={}



    # record whether a label was updated

    order={}

    newvalue={}

    edge=[]

    runfirsttime=1



    # populate an initial labels dictionary

    for i in net.keys():

        if i == s: labels[i] = 0 # shortest distance form s to s is 0

        else: labels[i] = float("inf") # initial labels are infinity

    from copy import copy

    drop1 = copy(labels) # used for looping

    newlabel=copy(labels)

    ## begin algorithm





    while len(drop1) > 0:

        # find the key with the lowest label

        minNode = min(drop1, key = drop1.get) #minNode is the node with the smallest label



        newneighbor=nearesttime(time,nodes,check,minNode,s)

        caltime=0

        # update labels for nodes that are connected to minNode

        for i in net[minNode]:

##            print minNode

##            print i

##            print newneighbor

##            print time[nodes.index(minNode)]

            if labels[i] > (labels[minNode] + net[minNode][i]):

                newneighbor=nearesttime(time,nodes,check,i,minNode)

                #print newneighbor

                a=caltime

                #print(str(newneighbor[0])+' '+str(time[nodes.index(s)][1]))

                if time[nodes.index(minNode)][0]<=newneighbor[1] and newneighbor[0]<=time[nodes.index(minNode)][1]: #if overlaping

                    caltime=0

                else: #adding time with weight if not overlaping

                    if newneighbor[0]==time[nodes.index(minNode)][0]:

                        caltime=0

                    else:



                        if newneighbor[0]<time[nodes.index(minNode)][0]:

                            caltime=(24+newneighbor[0]-time[nodes.index(minNode)][1])*3600 # Change it to 60 if adjacent nodes timings are in minutes.

                        else:

                            caltime=(abs(newneighbor[0]-time[nodes.index(minNode)][1])*3600) # Change it to 60 if adjacent nodes timings are in minutes.

                #print caltime

                if newlabel[i] > (newlabel[minNode] + net[minNode][i])+a:

                    newlabel[i]= newlabel[minNode]+net[minNode][i]+caltime

##                if i=='v1' or i=='v2':

##                    print newlabel[i]

##                    print 'hi'

                labels[i] = labels[minNode] + net[minNode][i]

                drop1[i] = labels[minNode] + net[minNode][i]

                order[i] = minNode

                #print (str(labels[minNode]) +' '+str(net[minNode][i])+' '+ str(caltime))

                #print newvalue

                for key,value in net.iteritems():

                        if key==minNode:

                            for k,val in value.iteritems():

                                if val==net[minNode][i]:

                                    edge.append(k)

            #print '_________________________________________'

        del drop1[minNode] # once a node has been visited, it's excluded from drop1

        #print '++++++++++++++++++++++++++++++++++++++++++++++++++'

    ## end algorithm

    # print shortest path

    temp = copy(t)

    rpath = []

    path = []

    while 1:

        rpath.append(temp)

        if order.has_key(temp): temp = order[temp]

        else: return "There is no path from " + str(s) + " to " + str(t) + "."

        if temp == s:

            rpath.append(temp)

            break

    for j in range(len(rpath)-1,-1,-1):

        
        path.append(rpath[j])
        
    for i in xrange(len(edge)):

        if t==edge[i]:
          
            return s + "," + t + "," + str(path) + "," + str(newlabel[t])

# Given a large random network find the shortest path from '0' to '5'

## Cosine Similarity Calculation

def Jaccard(str1, str2):

    str1 = set(str1.split())

    str2 = set(str2.split())

    return float(len(str1 & str2)) / len(str1 | str2)

def get_cosine(vec1, vec2):

     

     intersection = set(vec1.keys()) & set(vec2.keys())

     numerator = sum([vec1[x] * vec2[x] for x in intersection])



     sum1 = sum([vec1[x]**2 for x in vec1.keys()])

     sum2 = sum([vec2[x]**2 for x in vec2.keys()])

     denominator = math.sqrt(sum1) * math.sqrt(sum2)



     if not denominator:

        return 0.0

     else:

        return float(numerator) / denominator



WORD = re.compile(r'\w+')

def text_to_vector(text):

     words = WORD.findall(text)

     return Counter(words)


## eigenvector centrality Calculation
    
def eigenvector_centrality(G, max_iter=100, tol=1.0e-6, nstart=None,
                           weight='weight'):
    

    from math import sqrt

    if type(G) == nx.MultiGraph or type(G) == nx.MultiDiGraph:

        raise nx.NetworkXException("Not defined for multigraphs.")



    if len(G) == 0:

        raise nx.NetworkXException("Empty graph.")



    if nstart is None:

        # choose starting vector with entries of 1/len(G)

        x = dict([(n,1.0/len(G)) for n in G])

    else:

        x = nstart

    # normalize starting vector

    s = 1.0/sum(x.values())

    for k in x:

        x[k] *= s

    nnodes = G.number_of_nodes()

    # make up to max_iter iterations

    for i in range(max_iter):

        xlast = x

        x = dict.fromkeys(xlast, 0)

        # do the multiplication y^T = x^T A

        for n in x:

            for nbr in G[n]:

                x[nbr] += xlast[n] * G[n][nbr].get(weight, 1)

        # normalize vector

        try:

            s = 1.0/sqrt(sum(v**2 for v in x.values()))

        # this should never be zero?

        except ZeroDivisionError:

            s = 1.0

        for n in x:

            x[n] *= s

        # check convergence

        err = sum([abs(x[n]-xlast[n]) for n in x])

        if err < nnodes*tol:

            return x



    raise nx.NetworkXError("""eigenvector_centrality():

power iteration failed to converge in %d iterations."%(i+1))""")


#Start Algorithim


a=1

#find centrality of node
if a==1:

    G=nx.Graph(net)

    centrality=eigenvector_centrality(G)

    total_score=[]

    final_score=[]



    flag=0

    while flag==0:

        source=asker

        if int(source)<len(nodes) and int(source)>=0:

            flag=1

        else:

            print ('No node found.')

            flag=0


    query=question


    j=0

    #find jackard and cosine

    for i in v:


        #jac=Jaccard(cat.upper(),str(i[0]).upper())

        vector1 = text_to_vector(query.upper())

        vector2 = text_to_vector(str(i[:]).upper())

##        print vector2
        
        cos=get_cosine(vector1, vector2)

##        print cos

        jac=0

        total=jac+cos

        total_score.append(total)

        j=j+1
      

##        print total_score

    for i in range(len(v)):

        final_score.append(total_score[i]*centrality[nodes[i]])

##    print final_score
   

    sort_ans=sorted(enumerate(final_score), key=lambda x: x[1])

    flag=0

    while flag==0:
        req_num =number_of_answerers

        if int(req_num)<=len(nodes) and int(req_num)>0:

            flag=1

        else:


            print ('Please enter valid inputs')

            flag=0
    final_ans=sort_ans[-(int(req_num)):]



    for i in range(len(final_ans)):

        if str(final_ans[i][0])==str(source):

            final_ans.remove(final_ans[i])

            final_ans=(sort_ans[-(int(req_num)+1):-(int(req_num))])+final_ans


##    print final_ans

    check=2

    flag=0

    while flag==0:

        st_time=int(start_time)

        st_end= int(end_time)

        a=[st_time,st_end]

        if st_time<=24 and st_time>0  and st_end<=24 and st_end>0 and st_time!=st_end:

            flag=1

        else:

            print ('Please enter valid inputs.')

            flag=0
    reserve=time[int(source)]

    time[int(source)]=a

    distance=[]

check=1


for ind in final_ans:
    short=shortest_path_without_interval(net,check, s=str(nodes[int(source)]), t=str(nodes[ind[0]]))

    print short
    check=check+1


