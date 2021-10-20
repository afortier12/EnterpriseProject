/**
 * 
 */
 
const template = document.createElement('template');
template.innerHTML = `
 <div class="pure-menu pure-menu-horizontal">
 <a href="/ejb-project-web/LogoutServlet" class="pure-menu-heading pure-menu-link">Logout</a>
</div>`

document.body.insertBefore(template.content, document.body.firstChild);

