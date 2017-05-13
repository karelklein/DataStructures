import sys
from decimal import Decimal

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

def pageRank(outedges, inedges, pi, alpha):
	N = len(outedges)
	pi_next = [0 for _ in range(N)]
	constant = float(1-alpha)/N
	for p in range(N):
		sum = 0
		for edge in inedges[p]:
			e = int(edge)
			sum += float(pi[e]) / len(outedges[e])
		pi_next[p] = constant + alpha * sum
	return pi_next

def vecCompare(x,y):
	maxDiff = 0
	for i in range(len(x)):
		if abs(x[i] - y[i]) > maxDiff:
			maxDiff = abs(x[i] - y[i])
	return maxDiff

def converge(outedges, inedges, alpha, epsilon):
	N = len(outedges)
	pi_0 = [float(1)/N for _ in range(N)]
	pi_1 = pageRank(outedges, inedges, pi_0, alpha)
	i = 1
	while vecCompare(pi_1, pi_0) > epsilon:
		temp = pi_1
		pi_1 = pageRank(outedges, inedges, pi_1, alpha)
		pi_0 = temp
		i += 1
	pi_1format = []
	for i in pi_1:
		pi_1format.append('%.10e' %Decimal(i))
	return pi_1format

def main():
	alpha = float(sys.argv[2])
	o = outList(sys.argv[1])
	i = inList(o)
	PR = converge(o,i,alpha,1e-10)
	for pr in PR:
		print pr

if __name__ == '__main__':
	main()
	