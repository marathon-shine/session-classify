#!/usr/bin/python
# -*- coding: UTF-8 -*-
import threading
import jieba
import re

from jieba.analyse import *


class workerThread(threading.Thread):
    def __init__(self, cwd, name, f):
        threading.Thread.__init__(self)
        self.cwd = cwd
        self.name = name
        self.f = f
    def run(self):
    	print "Starting " + self.name
    	stopwords = {}.fromkeys([ line.rstrip() for line in open('stopword.txt') ])
    	try:
			while True:
				line = f.readline()
				if line:
					try:
						# 切分session
						ls = line.strip('\n').split("#####")
						# 清洗content
						content = ls[-1].replace(' ','').replace('、','/').replace('!','').replace('?','').replace('～','').replace('。','')
						# 剔除url
						urlExp = re.compile(r'https?://(?:[-\w.]|(?:%[\da-fA-F]{2}))+', re.S)
						content = urlExp.sub("", content)
						# 分词：精确模式
						segs = jieba.cut(content, cut_all=False)
						if segs:
							# 分词结果拼接回session
							words = []
							for seg in segs:
								seg = seg.encode('utf-8')
								if seg not in stopwords:
									words.append(seg)
							parse = ls[0] + '#####' + ' '.join(words) + '\n'

							cwd.write(parse)
							kw.write(' '.join(words) + '\n')
							print self.name + ": " + parse
					except Exception as ee:
						print ee
						continue
				else:
					break
        except Exception as e:
            print e



jieba.enable_parallel(4) # 开启并行分词模式，参数为并行进程数

f = open('input.txt', 'r') # 原始语料文件
cwd = open('output_session.txt', 'w') # cwd: chinese words digestion 分词后结果
kw = open('output_words.txt', 'w') # 纯词库，计算词频用
 
# 创建线程
thread1 = workerThread(cwd, "Thread-1", f)
thread2 = workerThread(cwd, "Thread-2", f)


# 开启新线程
thread1.start()
thread2.start()

# 等待所有线程完成
thread1.join()
thread2.join()
print "Main Thread: Cut words finished"

# 关闭文件连接
f.close()
cwd.close()
kw.close()

# 统计词频
f = open('output_words.txt', 'r')
data = f.read()

try:
	for keyword, weight in textrank(data, withWeight=True, topK = 100):
		print('%s %s' % (keyword, weight))
except Exception as e:
	print e
finally:
	f.close()