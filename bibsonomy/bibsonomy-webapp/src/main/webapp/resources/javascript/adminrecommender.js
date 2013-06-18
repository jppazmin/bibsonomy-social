$(document).ready(function(){deselectAll(document.recConfig.activeRecs);
deselectAll(document.recConfig.disabledRecs)
});
function pushSelectedOptions(b,a){while(b.selectedIndex!=-1){selectedOption=b.options[b.selectedIndex];
newOption=new Option(selectedOption.text,selectedOption.value,false,false);
if(document.all){a.add(newOption,0)
}else{a.add(newOption,a.options[0])
}b.remove(b.selectedIndex);
b.focus()
}}function selectAll(b){b.focus();
for(var a=0;
a<b.length;
a++){b.options[a].selected=true
}}function deselectAll(b){b.focus();
for(var a=0;
a<b.length;
a++){b.options[a].selected=false
}}function openEditForm(){recommenderSelect=document.recommenderRemove.deleteRecIds;
selectedRecommender=recommenderSelect.options[recommenderSelect.selectedIndex];
selectedRecommender.selected=false;
if(recommenderSelect.selectedIndex==-1){hiddenSettingId=document.getElementById("editId");
hiddenSettingId.value=selectedRecommender.text.substr(0,selectedRecommender.text.indexOf("-")-1);
recurl=document.getElementById("editedRecurl");
recurl.value=selectedRecommender.value;
div=document.getElementById("recommenderEditDiv");
div.style.display="block"
}selectedRecommender.selected=true
};