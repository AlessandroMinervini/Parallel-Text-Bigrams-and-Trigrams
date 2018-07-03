function [] = plot_nGrammi_java()
%% Plot bigrammi

results_2grams_seq = csvread('seq_2grammi.csv');
results_2grams_seq = results_2grams_seq/1000;

results_2grams_parallel_2th = csvread('parallel_2grams_2thread.csv');
results_2grams_parallel_2th = results_2grams_parallel_2th/1000;

results_2grams_parallel_4th = xlsread('parallel_2grams_4thread.xlsx');
results_2grams_parallel_4th = results_2grams_parallel_4th/1000;

figure(1);
plot(results_2grams_seq,'LineWidth',2);
title('2-grams Java')
xticklabels({'50Kb','100Kb','200Kb','500Kb','1Mb','2Mb', '4Mb','8Mb', '16Mb', '32Mb'});
xlabel('Text dimension');
ylabel('Time (s)');
hold on;
plot(results_2grams_parallel_2th,'LineWidth',2);
plot(results_2grams_parallel_4th,'LineWidth',2);
legend({'Sequential','2 thread','4 thread'},'FontSize',12);
%% Pot Trigrammi

results_3grams_seq = xlsread('seq_3grams.xlsx');
results_3grams_seq = results_3grams_seq/1000;

parallel_3grams_2th = xlsread('parallel_3grams_2th.xlsx');
parallel_3grams_2th = parallel_3grams_2th/1000;

parallel_3grams_4th = xlsread('parallel_3grams_4th.xlsx');
parallel_3grams_4th = parallel_3grams_4th/1000;

figure(2);
plot(results_3grams_seq,'LineWidth',2);
title('3-grams Java')
xticklabels({'50Kb','100Kb','200Kb','500Kb','1Mb','2Mb', '4Mb','8Mb', '16Mb', '32Mb'});
xlabel('Text dimension');
ylabel('Time (s)');
hold on;
plot(parallel_3grams_2th,'LineWidth',2);
plot(parallel_3grams_4th,'LineWidth',2);
legend({'Sequential','2 thread','4 thread'},'FontSize',12);
%% Speedup Trigrammi

speed_t_2th = results_3grams_seq./parallel_3grams_2th;
speed_t_4th = results_3grams_seq./parallel_3grams_4th;

figure(3);
plot(speed_t_2th,'LineWidth',2);
title('Speed Up Trigrams')
xticklabels({'50Kb','100Kb','200Kb','500Kb','1Mb','2Mb', '4Mb','8Mb', '16Mb', '32Mb'});
xlabel('Text dimension');
ylabel('Speed Up');
hold on;
plot(speed_t_4th,'LineWidth',2);
%plot(parallel_3grams_4th,'LineWidth',2);
legend({'2 thread','4 thread'},'FontSize',12);
%% Speedup Bigrammi

speed_b_2th = results_2grams_seq./results_2grams_parallel_2th;
speed_b_4th = results_2grams_seq./results_2grams_parallel_4th;

figure(4);
plot(speed_b_2th,'LineWidth',2);
title('Speed Up Bigrams')
xticklabels({'50Kb','100Kb','200Kb','500Kb','1Mb','2Mb', '4Mb','8Mb', '16Mb', '32Mb'});
xlabel('Text dimension');
ylabel('Speed Up');
hold on;
plot(speed_b_4th,'LineWidth',2);
%plot(parallel_3grams_4th,'LineWidth',2);
legend({'2 thread','4 thread'},'FontSize',12);

end

