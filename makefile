JAVAC = javac
JAVA = java
MAIN_CLASS = Main

all: compile

compile:
	$(JAVAC) *.java

run:
	$(JAVA) $(MAIN_CLASS)

clean:
	rm -f *.class
