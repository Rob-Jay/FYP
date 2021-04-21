import re

f = open("./Summarizer/Scan.txt", "r")


text = f.read()
' '.join(e for e in text if e.isalnum())

re.sub("\text\text+" , " ", text)
print(text)