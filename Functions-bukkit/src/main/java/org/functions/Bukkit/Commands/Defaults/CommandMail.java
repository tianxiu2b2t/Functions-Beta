package org.functions.Bukkit.Commands.Defaults;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.functions.Bukkit.API.FPI;
import org.functions.Bukkit.API.Mail.MailCode;
import org.functions.Bukkit.Main.Functions;
import org.functions.Bukkit.Main.functions.Account;
import org.functions.Bukkit.Main.functions.Accounts;

import java.util.List;

public class CommandMail implements TabExecutor {
    public void run() {
        Functions.instance.getAPI().getCommand("bindmail", new CommandMail());
        Functions.instance.getAPI().getCommand("recoverpassword", new CommandMail());
        Functions.instance.getAPI().getCommand("mailcode", new CommandMail());
        Functions.instance.getAPI().getCommand("mailogin", new CommandMail());
    }
    FPI fpi = Functions.instance.getAPI();
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Accounts.enable()) {
            sender.sendMessage(Accounts.noEnable());
            return true;
        }
        if (sender instanceof Player) {
            MailCode code;
            Player p = ((Player) sender).getPlayer();
            Account account = new Account(p.getUniqueId());
            if (fpi.hasAliases("BindMail", label)) {
                if (args.length < 2) {
                    sender.sendMessage(fpi.subcmd());
                    return true;
                }
                if (!account.isLogin()) {
                    sender.sendMessage(fpi.putLanguage("NotLoginBindMail", "&c请登录后再绑定邮箱吧",p));
                    return true;
                }
                if (account.existsMail()) {
                    if (account.getMail().equals(args[1])) {
                        sender.sendMessage(fpi.putLanguage("BindMailIsEquals", "&a邮箱不能于之前邮箱一样！",p));
                        return true;
                    }
                    if (account.setMail(args[0])) {
                        sender.sendMessage(fpi.putLanguage("BindMailSuccessfully", "&a成功绑定邮箱！",p));
                        return true;
                    }
                }
                if (!args[0].equals(args[1])) {
                    sender.sendMessage(fpi.putLanguage("BindMailNoEquals", "&c邮箱不一！",p));
                    return true;
                }
                if (account.setMail(args[0])) {
                    sender.sendMessage(fpi.putLanguage("BindMailSuccessfully", "&a成功绑定邮箱！",p));
                    return true;
                }
                return true;
            }
            if (fpi.hasAliases("RecoverPassword", label)) {
                if (!account.existsMail()) {
                    sender.sendMessage(fpi.putLanguage("NotMail", "&c没有找到你的邮箱。",p));
                    return true;
                }
                if (account.isLogin()) {
                    sender.sendMessage(fpi.putLanguage("LoginSendCode", "&c已登陆，不需要找回密码或登陆了！",p));
                    return true;
                }
                code = new MailCode(p.getUniqueId());
                sender.sendMessage(fpi.putLanguage("SendCode", "&a正在发送到你的邮箱%mail%.",p).replace("%mail%", account.getMail()));
                if (code.create()) {
                    sender.sendMessage(fpi.putLanguage("SendCodeSuccessfully", "&a成功发送到你的邮箱%mail%.",p).replace("%mail%", account.getMail()));
                    return true;
                }
                sender.sendMessage(fpi.putLanguage("SendCodeFailed", "&c发送失败（原因：已发送验证码，服务器没有开邮箱找回密码或邮箱错误！）",p));
                return true;
            }
            if (fpi.hasAliases("mailcode", label)) {
                if (args.length == 0) {
                    sender.sendMessage(fpi.subcmd());
                    return true;
                }
                code = new MailCode(p.getUniqueId());
                if (code.verify(args[0])) {
                    sender.sendMessage(fpi.putLanguage("CodeVerify", "&a验证码验证成功！快使用/cp <密码> <密码>来换密码吧",p));
                    return true;
                }
                sender.sendMessage(fpi.putLanguage("NotFoundCode","&c无效验证码或已过时间！",p));
                return true;
            }
            if (fpi.hasAliases("mailogin", label)) {
                if (args.length == 0) {
                    if (!account.existsMail()) {
                        sender.sendMessage(fpi.putLanguage("NotMail", "&c没有找到你的邮箱。",p));
                        return true;
                    }
                    if (account.isLogin()) {
                        sender.sendMessage(fpi.putLanguage("LoginSendCode", "&c已登陆，不需要找回密码或登陆了！",p));
                        return true;
                    }
                    code = new MailCode(p.getUniqueId());
                    sender.sendMessage(fpi.putLanguage("SendCode", "&a正在发送到你的邮箱%mail%.",p).replace("%mail%", account.getMail()));
                    if (code.create()) {
                        sender.sendMessage(fpi.putLanguage("SendCodeSuccessfully", "&a成功发送到你的邮箱%mail%.",p).replace("%mail%", account.getMail()));
                        return true;
                    }
                    sender.sendMessage(fpi.putLanguage("SendCodeFailed", "&c发送失败（原因：已发送验证码，服务器没有开邮箱找回密码或邮箱错误！）",p));
                    return true;
                }
                code = new MailCode(p.getUniqueId());
                if (code.verify(args[0])) {
                    if (account.mailLogin()) {
                        sender.sendMessage(fpi.putLanguage("CodeLogin", "&a验证码登陆成功！",p));
                        return true;
                    }
                    sender.sendMessage(fpi.putLanguage("CodeLoginFailed","&c验证码登陆失败！",p));
                    return true;
                }
                sender.sendMessage(fpi.putLanguage("NotFoundCode","&c无效验证码或已过时间！",p));
                return true;
            }
        }
        sender.sendMessage(fpi.noPlayer());
        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
