JCC = javac
JFLAGS = -g
SRC_DIR = src
BIN_DIR = bin
MAIN_CLASS = rw.ReadersWriters
SOURCES = $(shell find $(SRC_DIR) -name *.java)
CLASSES = $(SOURCES:$(SRC_DIR)/%.java=$(BIN_DIR)/%.class)

all: $(CLASSES)

$(BIN_DIR)/.sentinel: $(SOURCES)
	@mkdir -p $(BIN_DIR)
	@echo "Compiling Java sources..."
	$(JCC) $(JFLAGS) -d $(BIN_DIR) -sourcepath $(SRC_DIR) $(SOURCES)
	@touch $(BIN_DIR)/.sentinel

$(BIN_DIR)/%.class: $(BIN_DIR)/.sentinel
	@
	@

run: all
	@echo "Running project..."
	java -cp $(BIN_DIR) $(MAIN_CLASS)

clean:
	@echo "Cleaning compiled files..."
	rm -rf $(BIN_DIR)

.PHONY: all run clean
