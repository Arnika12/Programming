''' Functions #10: skyline
Define a function called myfunc that takes in a string, and returns a matching string where every even letter is uppercase, 
and every odd letter is lowercase. Assume that the incoming string only contains letters, and don't worry about numbers, 
spaces or punctuation. The output string can start with either an uppercase or lowercase letter, so long as letters alternate 
throughout the string. '''

def myfunc(s):
    return ''.join(c.upper() if i % 2 == 0 else c.lower() for i, c in enumerate(s))
