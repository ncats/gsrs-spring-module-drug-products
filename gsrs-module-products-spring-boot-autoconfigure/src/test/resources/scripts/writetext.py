import sys

fileName = sys.argv[1]
outputText= sys.argv[2]
with open(fileName, 'w', encoding='utf-8') as f:
    f.write(outputText)