import os.path
from typing import List

import jpype
import jpype.imports
from jpype.types import *

class OldWrapper:
   __instance = None
   @staticmethod
   def getInstance():
      """ Static access method. """
      if OldWrapper.__instance == None:
         OldWrapper()
      return OldWrapper.__instance

   def __init__(self):
      """ Virtually private constructor. """
      if OldWrapper.__instance != None:
         raise Exception("This class is a singleton!")
      else:
          import StanfordNLPExtractor.resources
          jar = os.path.join(StanfordNLPExtractor.resources.__path__[0], "StanfordNLPExtractor.jar")
          self.jar = str(jar)
          jpype.startJVM(classpath=[self.jar])
          from uk.ncl.giacomobergami import SNLP_Python
          self.this = SNLP_Python()
          OldWrapper.__instance = self

   def generateGSMDatabase(self, sentences:List[str], start:str=None, end:str=None):
        jTokens = jpype.java.util.ArrayList()
        if sentences is None:
            sentences = []
        sentences = list(sentences)
        for sentence in sentences:
            jTokens.add(jpype.java.lang.String(str(sentence)))
        if start is not None:
            start = jpype.java.lang.String(str(start))
        if end is not None:
            end = jpype.java.lang.String(str(end))
        return self.this.generateGSMDatabase(start, end, jTokens)

   def getTimeUnits(self, sentences:List[str]):
        jTokens = jpype.java.util.ArrayList()
        if sentences is None:
            sentences = []
        sentences = list(sentences)
        for sentence in sentences:
            jTokens.add(jpype.java.lang.String(str(sentence)))
        return self.this.getTimeUnits(jTokens)