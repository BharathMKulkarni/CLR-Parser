import re

# REGEXs TO MATCH
regex_digit = '^[0-9]+$'
regex_float = '[+-]?([0-9]*[.])?[0-9]+[E-]?[E+]?[0-9]'
regex_names = '[a-zA-Z][a-zA-Z0-9]*'
regex_string = '["][ ]*[a-zA-Z0-9]*[ ]*[a-zA-Z0-9]*[ ]*["]'

SYMBOLS = ['(',
           ')',
           ';',
           ',',
           ':',
           '\'']

KEYWORDS = {'t': ['INTEGER', 'REAL','STRING','int'],
            'm': ['main'],
            'b': ['BEGIN','Begin','begin'],
            'p': ['PRINT'],
            'e': ['END','End','end'],
            's': [' '," "],
            'o': ['+', '-', ':=','=','>','/'],
            'n': ['\n'],
            'f': ['FOR','for'],
            'u': ['TO','to'],
            'z': ['do'],
            'd': ['2','4','6','-3.56E-8','4.567','"text1"','"hello"','1','5','"HELLO"','"Strings"'],
            'w': ['while'],
            'r':['return']
            }

OPERATORS = ['+', '-', '=',':=','>']

def getIndex(word):
    keys = list(KEYWORDS.keys())
    values = list(KEYWORDS.values())
    for value in values:
        if word in value:
            i = values.index(value)
            return keys[i]



def generate_tokens2(filename):
    tokens = []
    str_occ=''
    flag =False
    F = open(filename,"r")
    for line in F:
        linelist = line.split() 
        for i in range(len(linelist)):
            word = linelist[i]
            j=i
            if(word[0]=='"'):
                flag = True
                # while(j<len(linelist)):'
                while(flag):
                    if(linelist[j][-1]=='"'):
                        print(linelist[j])
                        str_occ+=linelist[j]+" "
                        print(str_occ)
                        tokens.append('d')
                        str_occ=''
                        flag = False
                        # break
                    else:
                        str_occ+=linelist[j]+" "
                        j+=1
                # word = str_occ
                continue
            i=j+1
            
            token = getIndex(word)
            if(re.search(regex_digit,word)):
                tokens.append('d')
                continue
            if(re.search(regex_float,word)):
                tokens.append('d')
                continue
            if token in KEYWORDS:
                tokens.append(token)
            elif word in OPERATORS:
                tokens.append('o')
            elif word in SYMBOLS:
                tokens.append(word)
            elif(re.search(regex_names,word)):
                tokens.append('v')
                continue
            
            

    token_string = "".join(tokens)
    
    return token_string



def generate_tokens(filename):
    tokens = []
    F = open(filename,"r")
    for line in F:
        for word in line.split():
            token = getIndex(word)
            if(re.search(regex_digit,word)):
                tokens.append('d')
                continue
            if(re.search(regex_float,word)):
                tokens.append('d')
                continue
            if token in KEYWORDS:
                tokens.append(token)
            elif word in OPERATORS:
                tokens.append('o')
            elif word in SYMBOLS:
                tokens.append(word)
            elif(re.search(regex_names,word)):
                tokens.append('v')
                continue

    token_string = "".join(tokens)
    
    return token_string


generate_tokens("input_code_7.txt")