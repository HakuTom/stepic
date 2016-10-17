import java.util.logging.Level;
import java.util.logging.Logger;



public class Mail {
    public static final String AUSTIN_POWERS = "Austin Powers";
    public static final String WEAPONS = "weapons";
    public static final String BANNED_SUBSTANCE = "banned substance";

    public static interface Sendable {
        String getFrom();

        String getTo();
    }

    public static abstract class AbstractSendable implements Sendable {

        protected final String from;
        protected final String to;

        public AbstractSendable(String from, String to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public String getFrom() {
            return from;
        }

        @Override
        public String getTo() {
            return to;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            AbstractSendable that = (AbstractSendable) o;

            if (!from.equals(that.from)) return false;
            if (!to.equals(that.to)) return false;

            return true;
        }

    }

    public static class MailMessage extends AbstractSendable {

        private final String message;

        public MailMessage(String from, String to, String message) {
            super(from, to);
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            MailMessage that = (MailMessage) o;

            if (message != null ? !message.equals(that.message) : that.message != null) return false;

            return true;
        }

    }

    public static class MailPackage extends AbstractSendable {
        private final Package content;

        public MailPackage(String from, String to, Package content) {
            super(from, to);
            this.content = content;
        }

        public Package getContent() {
            return content;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            MailPackage that = (MailPackage) o;

            if (!content.equals(that.content)) return false;

            return true;
        }

    }

    public static class Package {
        private final String content;
        private final int price;

        public Package(String content, int price) {
            this.content = content;
            this.price = price;
        }

        public String getContent() {
            return content;
        }

        public int getPrice() {
            return price;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Package aPackage = (Package) o;

            if (price != aPackage.price) return false;
            if (!content.equals(aPackage.content)) return false;

            return true;
        }
    }

    public static interface MailService {
        Sendable processMail(Sendable mail);
    }

    public static class RealMailService implements MailService {

        @Override
        public Sendable processMail(Sendable mail) {
            // Здесь описан код настоящей системы отправки почты.
            return mail;
        }
    }

    public static class IllegalPackageException extends RuntimeException{

    }

    public static class StolenPackageException extends RuntimeException{

    }

    public static class Spy implements MailService{
        private Logger logger;
        public Spy(Logger logger){
            this.logger = logger;
        }
        @Override
        public Sendable processMail(Sendable mail){
            if (mail instanceof MailMessage){
                if(mail.getFrom().equals(AUSTIN_POWERS) || mail.getTo().equals(AUSTIN_POWERS) ) {
                    logger.log(Level.WARNING
                            , "Detected target mail correspondence: from {0} to {1} \"{2}\""
                            , new Object[]{mail.getFrom(),
                                          mail.getTo(),
                                          ((MailMessage) mail).getMessage()
                                          }
                    );
                } else {
                    logger.log(Level.INFO
                            , "Usual correspondence: from {0} to {1}"
                            , new Object[]{mail.getFrom(), mail.getTo() }
                            );
                }
            }
            return mail;
        }
    }

    public static class Thief implements MailService{
        private static int stolen = 0;
        private int minPrice;

        public Thief(int minPrice){
            this.minPrice = minPrice;
        }

        public int getStolenValue(){
            return stolen;
        }

        @Override
        public Sendable processMail(Sendable mail){
            if (mail instanceof MailPackage) {
                if (((MailPackage) mail).getContent().getPrice() >= minPrice) {
                    stolen += ((MailPackage) mail).getContent().getPrice();
                    String parcel = "stones instead of "+((MailPackage) mail).getContent().getContent();
                    return new MailPackage(mail.getFrom(), mail.getTo(), new Package(parcel,0));
                }
            }
            return mail;

        }
    }

    public static class Inspector implements MailService{
        @Override
        public Sendable processMail(Sendable mail){
            if( !(mail instanceof MailPackage)) return mail;
            String cont = ((MailPackage) mail).getContent().getContent();
            if(cont.contains(WEAPONS) || cont.contains(BANNED_SUBSTANCE))
                throw new IllegalPackageException();
            if(cont.contains("stones"))
                throw new StolenPackageException();
            return mail;
        }
    }

    public static class UntrustworthyMailWorker implements MailService {
        private MailService[] mailServiceS;
        private RealMailService realMail = new RealMailService();

        public UntrustworthyMailWorker() {}

        public UntrustworthyMailWorker(MailService[] mailServiceS ){
            this.mailServiceS = mailServiceS;
        }

        public RealMailService getRealMailService(){
            return realMail;
        }

        @Override
        public Sendable processMail(Sendable mail) {
            for(MailService MS : mailServiceS)
                mail = MS.processMail(mail);
            return getRealMailService().processMail(mail);
        }
    }
}
