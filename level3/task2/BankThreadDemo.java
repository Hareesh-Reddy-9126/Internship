public class BankThreadDemo {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(1000);

        Thread t1 = new UserThread(account, 700, "User-1");
        Thread t2 = new UserThread(account, 700, "User-2");

        t1.start();
        t2.start();
    }
}

class BankAccount {
    private int balance;

    public BankAccount(int initialBalance) {
        this.balance = initialBalance;
    }

    public synchronized void withdraw(int amount) {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " is attempting to withdraw " + amount);

        if (balance >= amount) {
            try {
                Thread.sleep(100); // 100 ms
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            balance -= amount;
            System.out.println(threadName + " withdrawal successful. Remaining balance: " + balance);
        } else {
            System.out.println(threadName + " cannot withdraw. Insufficient balance: " + balance);
        }
    }
}

class UserThread extends Thread {
    private final BankAccount account;
    private final int amount;

    public UserThread(BankAccount account, int amount, String name) {
        super(name); // set thread name
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void run() {
        account.withdraw(amount);
    }
}

