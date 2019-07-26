# -*- coding=utf-8 -*-
import jieba
from jieba.analyse import *

f = open("/Users/wanghuan/Desktop/new_message_[694917].txt",'r')
data = f.read()

# for keyword, weight in extract_tags(data, withWeight=True):
#     print('%s %s' % (keyword, weight))


for keyword, weight in textrank(data, withWeight=True):
    print('%s %s' % (keyword, weight))

# tags = jieba.analyse.extract_tags(f,topK = 10)

# print("Keyword:" + "/".join(tags))


# with open('/Users/wanghuan/Desktop/raw.txt', 'r', encoding="UTF-8") as file1:  # with as操作读取文件很ok
#     content = "".join(file1.readlines())

# with open('/Users/wanghuan/Desktop/keywords.txt', 'w', encoding='utf-8') as file2:
#     file2.write(content_after+"\n")

# # 调用jieba.cut
# sentence_seged = jieba.cut(content)

# # stopwords为停用词list
# stopwords = [line.strip() for line in open('stop.txt', 'r', encoding='utf-8').readlines()]

# outstr = '' # 待返回字符串

#  for word in sentence_seged:
#     if word not in stopwords:
#         outstr += word + " "


# # 去除停用词
# stopwords = {}.fromkeys(['的', '包括', '等', '是'])
# text = "故宫的著名景点包括乾清宫、太和殿和午门等。其中乾清宫非常精美，午门是紫禁城的正门。"
# # 精确模式
# segs = jieba.cut(text, cut_all=False)
# final = ''
# for seg in segs:
#     if seg not in stopwords:
#             final += seg
# print (final)

# seg_list = jieba.cut(final, cut_all=False)
# print ("/ ".join(seg_list))

# stopwords = {}.fromkeys([ line.rstrip() for line in open('stopword.txt') ])

# # stopwords = {}.fromkeys(['的', '附近'])

# segs = jieba.cut('北京附近的租房', cut_all=False)
# final = ''
# for seg in segs:
#     seg = seg.encode('utf-8')
#     if seg not in stopwords:
#             final += seg
# print final




# # 全模式
# text = "我来到北京清华大学"
# seg_list = jieba.cut(text, cut_all=True)
# print(u"[全模式]: " +  "/ ".join(seg_list))

# # 精确模式
# seg_list = jieba.cut(text, cut_all=False)
# print(u"[精确模式]: ", "/ ".join(seg_list))

# # 默认是精确模式
# seg_list = jieba.cut(text)
# print(u"[默认模式]: ", "/ ".join(seg_list))

# # 搜索引擎模式
# seg_list = jieba.cut_for_search(text)
# print(u"[搜索引擎模式]: ", "/ ".join(seg_list))

# seg_list = jieba.cut("卧槽你行不行啊", cut_all=True, HMM=False)
# print("Full Mode: " + "/ ".join(seg_list))  # 全模式