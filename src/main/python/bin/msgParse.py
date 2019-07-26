#!/usr/bin/python
# -*- coding: UTF-8 -*-
import threading
import jieba
# import sys

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
						ls = line.strip('\n').split("###")
						# 清洗content
						content = ls[-1].replace(' ','').replace('、','/').replace('!','').replace('?','').replace('～','').replace('。','')
						# 分词：精确模式
						segs = jieba.cut(content, cut_all=False)
						if segs:
							parse = "###".join(ls[i] for i in range(0, len(ls) - 1))
							# 分词结果拼接回session
							words = []
							for seg in segs:
								seg = seg.encode('utf-8')
								if seg not in stopwords:
									words.append(seg)
							parse = parse + '###' + ' '.join(words) + '\n'

							cwd.write(parse)
							kw.write(' '.join(words) + '\n')
							print self.name + ": " + parse
					except:
						continue
				else:
					break
        except Exception as e:
            print e


f = open('new_message_[694917].txt', 'r') # 原始语料文件
cwd = open('parsed_session.txt', 'w') # cwd: chinese words digestion 分词后结果
kw = open('parsed_words.txt', 'w') # cwd: chinese words digestion 分词后结果
 
# 创建线程
thread1 = workerThread(cwd, "Thread-1", f)

# 开启新线程
thread1.start()

# 等待所有线程完成
thread1.join()
print "Main Thread: Cut words finished"

# 关闭文件连接
f.close()
cwd.close()
kw.close()