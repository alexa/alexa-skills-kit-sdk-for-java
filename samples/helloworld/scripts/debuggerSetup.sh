brew install jq
export WEBHOOK_URL="$(curl http://localhost:4040/api/tunnels | jq ".tunnels[0].public_url")"
read -p "Enter path of the local skill package: " skillpath
export SKILL_PATH=$skillpath
echo $SKILL_PATH >>~/.bash_profile
cd $SKILL_PATH
cp skill.json skill.json.debugger.bak
tmp=$(mktemp)
(jq ".manifest.apis.custom.endpoint.uri=$WEBHOOK_URL" skill.json) > "$tmp" && mv "$tmp" skill.json
ask deploy -t skill