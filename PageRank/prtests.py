import numpy as np
import matplotlib.pyplot as plt

def outList(fname):
	with open(fname,'r') as file:
		biggest = 0
		for line in file:
			for v in line.split(':')[1].split(','):
				if int(v) > biggest:
					biggest = int(v)
			if int(line.split(':')[0]) > biggest:
					biggest = int(line.split(':')[0])
	adjListout = []
	for i in range(biggest + 1):
		adjListout.append([i]) #self loop

	with open(fname,'r') as file:
		for line in file:
			vertex = int(line.split(':')[0])
			for v in line.split(':')[1].split(','):
				adjListout[vertex].append(int(v))
	return adjListout

def inList(outList):
	n = len(outList)
	adjListin = []
	for i in range(n):
		adjListin.append([i])
	for l in range(n):
		for v in outList[l]:
			if int(v) != l:
				adjListin[int(v)].append(l)
	return adjListin

def getPR(filename):
	prvector = []
	with open(filename, 'r') as file:
		for line in file:
			prvector.append(float(line))
	return prvector

def topk(vector, k):
	return sorted(vector, reverse=True)[:k]

def argtopk(vector, k):
	arr = np.array(vector).argsort()[::-1][:len(vector)]
	return arr[:k]

def diff(x,y):
	d = []
	maxd = 0
	for i in range(len(x)):
		z = abs(x[i] - y[i])
		d.append(z)
		if z > maxd:
			maxd = z
	return d, maxd

def l1norm(x,y):
	d = diff(x,y)
	sum = 0
	for i in d:
		sum += abs(float(i))
	return sum

def l2norm(x,y):
	d = diff(x,y)
	sum = 0
	for i in d:
		sum += float(i)**2
	return sum

def avg(vec):
	return float(sum(vec))/float(len(vec))

a75 = getPR('output_75.txt')
a80 = getPR('output_80.txt')
a85 = getPR('output.txt')
a90 = getPR('output_90.txt')
a95 = getPR('output_95.txt')
results = [a75, a80, a85, a90, a95]
alphas = [.75,.80,.85,.90,.95]

def plot_best_pr(k):
	for a in range(len(results)):
		plt.plot(range(1,11), topk(results[a],k), label='a = %.2f' %alphas[a])
	plt.legend(loc='upper right')
	plt.title('Top 10 PageRanks')
	plt.xlabel('Rank')
	plt.ylabel('PageRank value')
	plt.grid(True)
	plt.show()

def plot_inedgecount(k):
	argtop10 = []
	o = outList('large.txt')
	i = inList(o)
	for a in range(len(results)):
		argtop10.append(argtopk(results[a],k))
	edgecount = []
	for x in range(len(argtop10)):
		xlist = []
		for e in range(len(argtop10[x])):
			inedgeCount = len(i[argtop10[x][e]])
			xlist.append(inedgeCount)
		edgecount.append(xlist)

	for x in range(len(edgecount)):
		plt.plot(range(1,11), edgecount[x], label='a= %.2f' %alphas[x])
	plt.legend(loc='upper left')
	plt.title('# of In-Edges for Top 10 PageRanks')
	plt.xlabel('Rank')
	plt.ylabel('Number of Incoming Edges')
	plt.grid(True)
	plt.show()

def plot_changes():
	maxchange = []
	for v in range(1,len(results)):
		maxchange.append(diff(results[v], results[v-1])[1])
	plt.plot(alphas[1:], maxchange)
	plt.title('Max Change in PR with Change in alpha')
	plt.xlabel('alpha')
	plt.ylabel('Max Change in PageRank (from previous alpha)')
	plt.grid(True)
	plt.show()

def plot_avgs(klist):
	allavgs = []
	for k_ in klist:
		avgs = []
		for a in range(len(results)):
			topvec = topk(results[a],k_)
			avgs.append(avg(topvec))
		allavgs.append(avgs)
	for i in range(len(klist)):
		plt.plot(alphas, allavgs[i], label='k = %i' %klist[i])
	plt.title('Average PageRank for Top k sites')
	plt.xlabel('alpha')
	plt.ylabel('Average PageRank')
	plt.legend(loc='upper left')
	plt.grid(True)
	plt.show()

def plot_converge():
	iterations = [43,53,70,103,186]
	plt.plot(alphas, iterations)
	plt.title('Iterations to Convergence')
	plt.xlabel('alpha')
	plt.ylabel('# of Iterations')
	plt.grid(True)
	plt.show()
	

plot_converge()
#plot_avgs([10,50,100,500,1000])
#plot_changes()
#plot_inedgecount(10)
#plot_best_pr(10)