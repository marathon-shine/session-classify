# -*- coding: UTF-8 -*-
import sys
import jieba
 
def func(a,b):
    return (a+b)
 
if __name__ == '__main__':
    content = sys.argv[1]
    try:
    	stopwords = {}.fromkeys([ line.rstrip() for line in open('/Users/wanghuan/Desktop/cut/stopword.txt') ])
    except Exception as e:
    	print e

    segs = jieba.cut(content, cut_all=False)
    if segs:	
    	try:
    		words = []
    		for seg in segs:
				seg = seg.encode('utf-8')
				if seg not in stopwords:
					words.append(seg)
    		result = ' '.join(words)
    		print(result)
			# print(u"[全模式]: " +  "/ ".join(words))
    	except Exception as e:
    		print e
