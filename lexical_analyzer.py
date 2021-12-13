import re
regex_digit = '^[0-9]+$'
regex_float = '[+-]?([0-9]*[.])?[0-9]+[E-]?[E+]?[0-9]'
regex_names = '^[a-zA-Z_$][a-zA-Z_$0-9]*$'
SYMBOLS = ['(',
           ')',
           ';',
           ',',
           ':',
           '\'']

KEYWORDS = {'t': ['INTEGER', 'REAL','STRING'],
            'b': ['BEGIN'],
            'p': ['PRINT'],
            'e': ['END'],
            's': [' '," "],
            'o': ['+', '-', ':='],
            'n': ['\n'],
            'f': ['FOR'],
            'u': ['TO'],
            'd': ['2','4','6','-3.56E-8','4.567','"text1"','"hello"','1','5','"HELLO"','"Strings"']
            }

OPERATORS = ['+', '-', '=',':=']

def getIndex(word):
    keys = list(KEYWORDS.keys())
    values = list(KEYWORDS.values())
    for value in values:
        if word in value:
            i = values.index(value)
            return keys[i]



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


generate_tokens("input_code.txt")