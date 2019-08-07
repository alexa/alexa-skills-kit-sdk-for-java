#read -p "Enter path of the local skill package: " skillpath
SKILL_PATH="/Users/sattwikp/DevCentral/Skillsdev/HelloWorld"
cd $SKILL_PATH
cp skill.json.debugger.bak skill.json
rm skill.json.debugger.bak
ask deploy -t skill